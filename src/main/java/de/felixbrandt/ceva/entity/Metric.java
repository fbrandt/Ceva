package de.felixbrandt.ceva.entity;

import java.util.HashMap;
import java.util.List;

/**
 * Typed metric to calculate.
 */
@SuppressWarnings("designforextension")
public abstract class Metric extends Rule
{
  private static final long serialVersionUID = 1L;
  private MetricType type;

  public Metric()
  {
    super();
    init();
  }

  public Metric(final String name)
  {
    super(name);
    init();
  }

  private void init ()
  {
    setType(MetricType.UNKNOWN_METRIC);
  }

  public int getMetric ()
  {
    return getId();
  }

  public void setMetric (final int metric)
  {
    setId(metric);
  }

  public MetricType getType ()
  {
    return type;
  }

  public void setType (final MetricType _type)
  {
    type = _type;
  }

  public String getDataEntity ()
  {
    throw new RuntimeException("this code must never be reached");
  }
  public String getSourceReference ()
  {
    throw new RuntimeException("this code must never be reached");
  }

  @Override
  public void updateRuleDetails (final Rule from)
  {
    if (from instanceof Metric) {
      final Metric from_metric = (Metric) from;

      setType(from_metric.getType());
      updateMetricDetails(from_metric);
    }
  }

  public final HashMap<String, List<String>> getParameters ()
  {
    return new HashMap<String, List<String>>();
  }

  public void updateMetricDetails (final Metric from_metric)
  {

  }
}
