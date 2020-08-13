package de.felixbrandt.ceva;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.hibernate.exception.DataException;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.ceva.provider.UnsolvedProvider;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Save Solution in database.
 */
public class SolutionDBStorage implements Storage, UnsolvedProvider
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final SessionHandler session_handler;

  public SolutionDBStorage(final SessionHandler handler)
  {
    session_handler = handler;
  }

  public final boolean exists (final Executable executable, final DataSource source,
          final int version)
  {
    assert executable instanceof AlgorithmExecutable;
    assert source instanceof InstanceSource;

    Algorithm algorithm = ((AlgorithmExecutable) executable).getAlgorithm();
    Instance instance = ((InstanceSource) source).getInstance();
    String parameters = executable.getParametersAsString();

    return exists(algorithm, instance, parameters, version);
  }

  public final boolean exists (final Algorithm algorithm, final Instance instance,
          final String parameters, final int version)
  {
    Query query = getQuery(algorithm, instance, parameters, version);
    final boolean exists = query.list().size() > 0;
    return exists;
  }

  public final void add (final Object data)
  {
    if (data instanceof Solution) {
      addIfNotExists((Solution) data);
    }
  }

  public final void addIfNotExists (final Solution solution)
  {
    final Algorithm algorithm = solution.getAlgorithm();

    if (algorithm.getRepeat() - count(algorithm, solution.getInstance(),
            solution.getParameters(), solution.getVersion()) > 0) {
      LOGGER.debug("saving data for algorithm {}, instance {}, version {}",
              algorithm.getName(), solution.getInstance().getName(), solution.getVersion());
      doAdd(solution);
    } else {
      LOGGER.debug("solution already exists for algorithm {}, instance {}, version {}",
              algorithm, solution.getInstance().getName(), solution.getVersion());
    }
  }

  public final void doAdd (final Solution solution)
  {
    try {
      session_handler.getSession().save(solution);
      session_handler.saveAndBegin();
    } catch (DataException e) {
      LOGGER.error(e.getMessage());
      LOGGER.error("dumping solution to file solution.jdbc.error");
      try {
        FileOutputStream error_stream = new FileOutputStream("solution.jdbc.error");
        ObjectOutputStream object_stream = new ObjectOutputStream(error_stream);
        object_stream.writeObject(solution);
        object_stream.close();
        error_stream.close();
      } catch (Exception f) {
        f.printStackTrace();
      }

      session_handler.rollbackAndBegin();
    }
  }

  public final HashSet<Integer> getUnsolved (final Executable executable, final int version)
  {
    if (executable instanceof AlgorithmExecutable) {
      return getUnsolved((AlgorithmExecutable) executable, version);
    }
    return new HashSet<Integer>();
  }

  public final HashSet<Integer> getUnsolved (final AlgorithmExecutable executable,
          final int version)
  {
    final String query = "SELECT i.instance FROM Instance AS i LEFT JOIN Solution AS s "
            + "ON (i.instance = s.instance AND s.algorithm = :rule AND"
            + " s.version = :version AND s.parameters = :parameters) "
            + "GROUP BY i.instance HAVING COUNT(DISTINCT s.solution) < :repeat";

    session_handler.getSession().flush();
    final Query stmt = session_handler.getSession().createSQLQuery(query);

    stmt.setParameter("rule", executable.getAlgorithm());
    stmt.setParameter("version", version);
    stmt.setParameter("parameters", executable.getParametersAsString());
    stmt.setParameter("repeat", (long) executable.getRepeat());

    return new HashSet<Integer>(stmt.list());
  }

  public final int count (final Executable executable, final DataSource source,
          final int version)
  {
    assert executable instanceof AlgorithmExecutable;
    assert source instanceof InstanceSource;

    Algorithm algorithm = ((AlgorithmExecutable) executable).getAlgorithm();
    Instance instance = ((InstanceSource) source).getInstance();
    String parameters = executable.getParametersAsString();

    return count(algorithm, instance, parameters, version);
  }

  public final int count (final Algorithm algorithm, final Instance instance,
          final String parameters, final int version)
  {
    Query query = getQuery(algorithm, instance, parameters, version);
    return query.list().size();
  }

  public final Query getQuery (final Algorithm algorithm, final Instance instance,
          final String parameters, final int version)
  {
    final String sql = "from Solution where algorithm = :algorithm and parameters = :parameters "
            + "and instance = :instance and version = :version";

    final Query query = session_handler.getSession().createQuery(sql);

    query.setParameter("algorithm", algorithm);
    query.setParameter("parameters", parameters);
    query.setParameter("instance", instance);
    query.setParameter("version", version);

    return query;
  }
}
