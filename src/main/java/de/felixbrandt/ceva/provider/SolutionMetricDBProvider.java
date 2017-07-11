package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.SolutionMetricExecutable;

/**
 * Get SolutionMetric instances from database session.
 */
public class SolutionMetricDBProvider extends MetricDBProvider<SolutionMetric> implements
ExecutableProvider
{
  public SolutionMetricDBProvider(final SessionHandler handler)
  {
    super(handler);
  }

  public final Collection<? extends Executable> getExecutables ()
  {
    return SolutionMetricExecutable.generate(getMetrics());
  }

  @Override
  public final String getTablename ()
  {
    return "SolutionMetric";
  }

}
