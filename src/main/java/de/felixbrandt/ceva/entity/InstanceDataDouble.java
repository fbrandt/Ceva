package de.felixbrandt.ceva.entity;

/**
 * Integer result of an InstanceMetric execution.
 */
public class InstanceDataDouble extends DataDouble<InstanceMetric, Instance>
{
  private static final long serialVersionUID = 1L;

  public InstanceDataDouble()
  {
    super();
  }

  public InstanceDataDouble(final Instance instance, final InstanceMetric metric,
          final int version, final long value)
  {
    super(instance, metric, version, value);
  }

}
