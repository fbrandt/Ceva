package de.felixbrandt.ceva.entity;

/**
 * Metric to calculation for an Instance.
 */
public class InstanceMetric extends Metric
{
  private static final long serialVersionUID = 1L;

  public InstanceMetric()
  {
    super();
  }

  public InstanceMetric(final String name)
  {
    super(name);
  }

  public final String getDataEntity ()
  {
    switch (getType()) {
    case STRING_METRIC:
      return "InstanceDataString";
    case DOUBLE_METRIC:
      return "InstanceDataDouble";
    case INT_METRIC:
    default:
      return "InstanceDataInteger";
    }
  }
}
