package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.LazySolutionSource;

/**
 * Load Solution instances from database session.
 */
public class SolutionDBProvider implements DataSourceProvider, SolutionProvider
{
  private SessionHandler session_handler;

  public SolutionDBProvider(final SessionHandler handler)
  {
    session_handler = handler;
  }

  public final Collection<? extends DataSource> getDataSources ()
  {
    final String query = "SELECT s.solution FROM Solution AS s";
    session_handler.getSession().flush();
    final Query stmt = session_handler.getSession().createSQLQuery(query);

    return LazySolutionSource.generate(this, stmt.list());
  }

  public final Collection<Solution> getSolutions (final SessionHandler handler)
  {
    return handler.getSession().createQuery("from Solution").list();
  }

  public final Solution find (final int id)
  {
    final String query = "FROM Solution WHERE id = :id";
    final Query stmt = session_handler.getSession().createQuery(query);
    stmt.setParameter("id", id);

    final List<?> result = stmt.list();

    if (result.size() > 0) {
      return (Solution) stmt.iterate().next();
    }

    return null;
  }

  public final Collection<Solution> find (final Instance instance, final Algorithm algorithm)
  {
    final String query = "SELECT max(version) FROM Solution WHERE"
            + " instance = :instance AND algorithm = :algorithm";
    final Query stmt = session_handler.getSession().createQuery(query);
    stmt.setParameter("instance", instance);
    stmt.setParameter("algorithm", algorithm);

    int version = -1;
    try {
      version = (Integer) stmt.uniqueResult();
    } catch (final Exception e) {
      // no entry found => return empty list
      return new ArrayList<Solution>();
    }

    return find(instance, algorithm, version);
  }

  public final Collection<Solution> find (final Instance instance, final Algorithm algorithm,
          final int version)
  {
    final String query = "FROM Solution WHERE instance = :instance"
            + " AND algorithm = :algorithm AND version = :version";
    final Query stmt = session_handler.getSession().createQuery(query);
    stmt.setParameter("instance", instance);
    stmt.setParameter("algorithm", algorithm);
    stmt.setParameter("version", version);

    return stmt.list();
  }

}
