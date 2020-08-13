package de.felixbrandt.ceva;

import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.metric.MetricExecutable;
import de.felixbrandt.ceva.provider.DataProvider;
import de.felixbrandt.ceva.provider.UnsolvedProvider;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Save results of metric calculations in database.
 */
public abstract class DataDBStorage extends DataProvider implements Storage, UnsolvedProvider
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final SessionHandler session_handler;

  public DataDBStorage(final SessionHandler handler)
  {
    session_handler = handler;
  }

  protected final SessionHandler getSessionHandler ()
  {
    return session_handler;
  }

  public final boolean exists (final Executable executable, final DataSource source,
          final int version)
  {
    assert executable instanceof MetricExecutable;
    final Metric metric = ((MetricExecutable<?>) executable).getMetric();

    return exists(metric, source.getObject(), version);
  }

  @SuppressWarnings("unchecked")
  public final void add (final Object data)
  {
    if (data instanceof Data<?, ?>) {
      addIfNotExists((Data<? extends Metric, ?>) data);
    }
  }

  public final boolean exists (final Metric metric, final Object source, final int version)
  {
    final String query = "from " + getTablename(metric) + " where source = :source"
            + " and rule = :rule and version = :version";
    final Query stmt = session_handler.getSession().createQuery(query);

    stmt.setParameter("rule", metric);
    stmt.setParameter("source", source);
    stmt.setParameter("version", version);

    final boolean exists = stmt.list().size() > 0;

    return exists;
  }

  public final HashSet<Integer> getUnsolved (final Executable executable, final int version)
  {
    if (executable instanceof MetricExecutable) {
      return getUnsolved((MetricExecutable<?>) executable, version);
    }

    return new HashSet<Integer>();
  }

  public final HashSet<Integer> getUnsolved (final MetricExecutable<?> executable,
          final int version)
  {
    final String query = "SELECT i." + getSourceIDName() + " FROM " + getSourceObjectName()
            + " AS i " + "LEFT JOIN " + getTablename(executable.getMetric()) + " AS s "
            + "ON (s.source = i." + getSourceIDName()
            + " AND s.rule = :rule AND s.version = :version) "
            + "WHERE s.rule IS NULL LIMIT 1000";

    getSessionHandler().getSession().flush();
    final Query stmt = getSessionHandler().getSession().createSQLQuery(query);

    stmt.setParameter("rule", executable.getMetric());
    stmt.setParameter("version", version);

    return new HashSet<Integer>(stmt.list());
  }

  public String getTablename (final Metric metric)
  {
    return metric.getDataEntity();
  }

  public abstract String getSourceObjectName ();

  public abstract String getSourceIDName ();

  public final void addIfNotExists (final Data<? extends Metric, ?> data)
  {
    final Metric metric = data.getRule();

    if (!exists(metric, data.getSource(), data.getVersion())) {
      LOGGER.debug("saving data for metric {}, source {}, version {}", metric.getName(),
              data.getSource(), data.getVersion());
      doAdd(data);
    } else {
      LOGGER.debug("data already exists for metric {}, source {}, version {}",
              metric.getName(), data.getSource(), data.getVersion());
    }
  }

  public final void doAdd (final Data<?, ?> data)
  {
    session_handler.getSession().save(data);
    session_handler.saveAndBegin();
  }

  public final int count (final Executable executable, final DataSource source,
          final int version)
  {
    assert executable instanceof MetricExecutable;
    final Metric metric = ((MetricExecutable<?>) executable).getMetric();

    return count(metric, source.getObject(), version);
  }

  public final int count (final Metric metric, final Object source, final int version)
  {
    final String query = "from " + getTablename(metric) + " where source = :source"
            + " and rule = :rule and version = :version";
    final Query stmt = session_handler.getSession().createQuery(query);

    stmt.setParameter("rule", metric);
    stmt.setParameter("source", source);
    stmt.setParameter("version", version);

    return stmt.list().size();
  }

  @Override
  public final Collection<Data<?, ?>> getData (final Rule rule, final Collection<?> sources)
  {
    // get the latest version of the metric
    // this is a hack: H2 does not allow multi-column subqueries
    // so this will break if a metric version is ever >= 10000
    final String query = "FROM " + getTablename((Metric) rule)
            + " as o WHERE rule = :rule and (o.source * 10000 + o.version) IN "
            + "(SELECT source * 10000 + max(version) FROM " + getTablename((Metric) rule)
            + " WHERE rule = :rule and source IN (:sources) GROUP BY source)";
    final Query stmt = getSessionHandler().getSession().createQuery(query);

    stmt.setParameter("rule", rule);
    stmt.setParameterList("sources", sources);

    return stmt.list();
  }
}
