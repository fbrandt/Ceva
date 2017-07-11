package de.felixbrandt.ceva.metric;

import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.InstanceMetric;

/**
 * Wrap InstanceMetric to act as Executable.
 */
public class InstanceMetricExecutable extends MetricExecutable<InstanceMetric>
{
  private static final long serialVersionUID = 1L;

  public InstanceMetricExecutable(final InstanceMetric metric)
  {
    super(metric);
  }

  public final ContentMode getInputMode ()
  {
    return ContentMode.DEFAULT;
  }

  @Override
  public final ResultFactory getResultFactory ()
  {
    return new InstanceDataFactory(getMetric());
  }

  public static Collection<InstanceMetricExecutable> generate (
          final Iterable<InstanceMetric> metrics)
  {
    final Collection<InstanceMetricExecutable> result = new Vector<InstanceMetricExecutable>();

    for (final InstanceMetric metric : metrics) {
      result.add(new InstanceMetricExecutable(metric));
    }

    return result;
  }

}
