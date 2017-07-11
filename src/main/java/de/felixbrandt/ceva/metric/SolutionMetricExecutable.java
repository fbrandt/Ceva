package de.felixbrandt.ceva.metric;

import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.SolutionMetric;

/**
 * Wrap SolutionMetric to act as Executable.
 */
public class SolutionMetricExecutable extends MetricExecutable<SolutionMetric>
{
  private static final long serialVersionUID = 1L;

  public SolutionMetricExecutable(final SolutionMetric metric)
  {
    super(metric);
  }

  public final ContentMode getInputMode ()
  {
    return getMetric().getMode();
  }

  @Override
  public final ResultFactory getResultFactory ()
  {
    return new SolutionDataFactory(getMetric());
  }

  public static Collection<SolutionMetricExecutable> generate (
          final Iterable<SolutionMetric> metrics)
  {
    final Collection<SolutionMetricExecutable> result = new Vector<SolutionMetricExecutable>();

    for (final SolutionMetric metric : metrics) {
      result.add(new SolutionMetricExecutable(metric));
    }

    return result;
  }

}
