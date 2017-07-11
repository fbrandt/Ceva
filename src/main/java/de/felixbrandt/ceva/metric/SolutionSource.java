package de.felixbrandt.ceva.metric;

import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.support.StreamSupport;

/**
 * Wrap Solution to act as DataSource.
 */
public class SolutionSource extends DataSource implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private Solution solution;

  public SolutionSource(final Solution _solution)
  {
    solution = _solution;
  }

  @Override
  public final String getName ()
  {
    return "solution to " + solution.getInstance().getName();
  }

  @Override
  public final InputStream getContent (final ContentMode mode)
  {
    return StreamSupport.createInputStream(doGetContent(mode));
  }

  private String doGetContent (final ContentMode mode)
  {
    if (mode == ContentMode.STDERR) {
      return solution.getStderr();
    }

    return solution.getStdout();
  }

  public static Collection<DataSource> generate (final Iterable<Solution> solutions)
  {
    final Vector<DataSource> result = new Vector<DataSource>();

    for (final Solution solution : solutions) {
      result.add(new SolutionSource(solution));
    }

    return result;
  }

  @Override
  public final Object getObject ()
  {
    return solution;
  }

  @Override
  public final Integer getID ()
  {
    return solution.getSolution();
  }
}
