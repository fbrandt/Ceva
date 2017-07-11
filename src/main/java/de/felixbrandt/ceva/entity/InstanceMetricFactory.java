package de.felixbrandt.ceva.entity;

/**
 * Factory to setup InstanceMetric entities.
 */
public class InstanceMetricFactory extends MetricFactory
{
  @Override
  public final Rule doCreate ()
  {
    return new InstanceMetric();
  }
}
