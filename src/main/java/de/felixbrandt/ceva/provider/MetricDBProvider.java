package de.felixbrandt.ceva.provider;

import java.util.Collection;

import org.hibernate.query.Query;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Metric;

/**
 * Get InstanceMetric instances from database session.
 */
public abstract class MetricDBProvider<MetricType extends Metric> implements MetricProvider
{
  private final SessionHandler session_handler;

  public MetricDBProvider(final SessionHandler handler)
  {
    session_handler = handler;
  }

  public abstract String getTablename ();

  public final Collection<MetricType> getMetrics ()
  {
    return session_handler.getSession()
            .createQuery("from " + getTablename() + " where active = true").list();
  }

  public final Collection<MetricType> findByName (final String name)
  {
    final String query = "FROM " + getTablename() + " WHERE name LIKE :name";
    final Query stmt = session_handler.getSession().createQuery(query);

    if (name.equals("all") || name.equals("")) {
      stmt.setParameter("name", "%");
    } else {
      stmt.setParameter("name", name.replace("*", "%"));
    }

    return stmt.list();
  }
}
