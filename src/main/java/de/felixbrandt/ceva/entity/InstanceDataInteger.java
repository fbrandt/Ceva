package de.felixbrandt.ceva.entity;

/**
 * Integer result of an InstanceMetric execution.
 */
public class InstanceDataInteger extends DataInteger<InstanceMetric, Instance>
{
  private static final long serialVersionUID = 1L;

  public InstanceDataInteger()
  {
    super();
  }

  public InstanceDataInteger(final Instance instance, final InstanceMetric metric,
          final int version, final long value)
  {
    super(instance, metric, version, value);
  }

}
