package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;

/**
 * Provides preconfigured solution objects.
 */
public class FixedSolutionProvider implements SolutionProvider
{
  private Collection<Solution> result;
  private int last_id;
  private Instance last_instance;
  private Algorithm last_algorithm;
  private int last_version;

  public FixedSolutionProvider()
  {
    this(new ArrayList<Solution>());
  }

  public FixedSolutionProvider(final Collection<Solution> _result)
  {
    last_id = -1;
    result = _result;
  }

  public final Solution find (final int id)
  {
    last_id = id;

    if (result.size() == 0) {
      return null;
    }

    return result.iterator().next();
  }

  public final Collection<Solution> find (final Instance instance, final Algorithm algorithm)
  {
    last_instance = instance;
    last_algorithm = algorithm;

    return result;
  }

  public final Collection<Solution> find (final Instance instance, final Algorithm algorithm,
          final int version)
          {
    last_instance = instance;
    last_algorithm = algorithm;
    last_version = version;

    return result;
          }

  public final void add (final Solution solution)
  {
    result.add(solution);
  }

  public final int getLastId ()
  {
    return last_id;
  }

  public final Instance getLastInstance ()
  {
    return last_instance;
  }

  public final Algorithm getLastAlgorithm ()
  {
    return last_algorithm;
  }

  public final int getLastVersion ()
  {
    return last_version;
  }
}
