package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.metric.InstanceSource;

/**
 * Load Instance instances from database session.
 */
public class InstanceDBProvider implements DataSourceProvider, InstanceProvider
{
  private SessionHandler session_handler;
  private List<HQLFilter> instance_filters;

  public InstanceDBProvider(final SessionHandler handler)
  {
    this(handler, new ArrayList<HQLFilter>());
  }

  public InstanceDBProvider(final SessionHandler handler, List<HQLFilter> filters)
  {
    session_handler = handler;
    instance_filters = filters;
  }

  public final Collection<? extends DataSource> getDataSources ()
  {
    return InstanceSource.generate(getInstances(session_handler));
  }

  public final Collection<Instance> getInstances (final SessionHandler handler)
  {
    StringBuilder query = new StringBuilder();
    query.append("from Instance WHERE active = true");

    int prefix = 1;
    for (HQLFilter filter : instance_filters) {
      if (filter != null) {
        query.append(" AND " + filter.getWhereClause(Integer.toString(prefix)));
        prefix += 1;
      }
    }

    final Query stmt = handler.getSession().createQuery(query.toString());

    prefix = 1;
    for (HQLFilter filter : instance_filters) {
      if (filter != null) {
        filter.setParametersToQuery(stmt, Integer.toString(prefix));
        prefix += 1;
      }
    }

    Collection<Instance> result = stmt.list();

    return result;
  }

  public final Collection<Instance> findByKeyword (final String keyword)
  {
    final String query = "FROM Instance as i WHERE i.instance = :instance OR i.name LIKE :name";
    final Query stmt = session_handler.getSession().createQuery(query);
    String name_param = "";

    if (keyword.matches("\\d*")) {
      stmt.setParameter("instance", Integer.parseInt(keyword));
      name_param = keyword;
    } else {
      stmt.setParameter("instance", 0);
      name_param = "%" + keyword.replace("*", "%") + "%";
    }

    stmt.setParameter("name", name_param);

    return stmt.list();
  }

}
