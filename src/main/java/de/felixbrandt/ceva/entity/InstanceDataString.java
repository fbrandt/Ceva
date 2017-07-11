package de.felixbrandt.ceva.entity;

/**
 * Integer result of an InstanceMetric execution.
 */
public class InstanceDataString extends DataString<InstanceMetric, Instance>
{
  private static final long serialVersionUID = 1L;

  public InstanceDataString()
  {
    super();
  }

  public InstanceDataString(final Instance instance, final InstanceMetric metric,
          final int version, final String value)
  {
    super(instance, metric, version, value);
  }

}
