package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;

/**
 * Get InstanceMetric instances from database session.
 */
public class InstanceMetricDBProvider extends MetricDBProvider<InstanceMetric> implements
        ExecutableProvider
{
  public InstanceMetricDBProvider(final SessionHandler handler)
  {
    super(handler);
  }

  public final Collection<? extends Executable> getExecutables ()
  {
    return InstanceMetricExecutable.generate(getMetrics());
  }

  @Override
  public final String getTablename ()
  {
    return "InstanceMetric";
  }

}
