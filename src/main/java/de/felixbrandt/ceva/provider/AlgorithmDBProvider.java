package de.felixbrandt.ceva.provider;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;

/**
 * Load Algorithm instances from database session.
 */
public class AlgorithmDBProvider implements ExecutableProvider, AlgorithmProvider
{
  private SessionHandler session_handler;

  public AlgorithmDBProvider(final SessionHandler handler)
  {
    session_handler = handler;
  }

  public final Collection<? extends Executable> getExecutables ()
  {
    return AlgorithmExecutable.generate(getAlgorithms(session_handler));
  }

  public final Collection<Algorithm> getAlgorithms (final SessionHandler handler)
  {
    return handler.getSession().createQuery("from Algorithm where active = true").list();
  }

  public final Algorithm findByName (final String name)
  {
    final String query = "FROM Algorithm as a WHERE lower(a.name) = lower(:name)";
    final Query stmt = session_handler.getSession().createQuery(query);
    stmt.setParameter("name", name);

    final List<?> result = stmt.list();
    if (result.size() == 0) {
      return null;
    }

    return (Algorithm) result.get(0);
  }
}
