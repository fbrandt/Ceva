package de.felixbrandt.ceva.provider;

import java.util.Collection;

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

  public InstanceDBProvider(final SessionHandler handler)
  {
    session_handler = handler;
  }

  public final Collection<? extends DataSource> getDataSources ()
  {
    return InstanceSource.generate(getInstances(session_handler));
  }

  public final Collection<Instance> getInstances (final SessionHandler handler)
  {
    return handler.getSession().createQuery("from Instance where active = true").list();
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
