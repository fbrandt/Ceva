package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Metric;

/**
 * Provides a preconfigured metrics.
 */
public class FixedMetricProvider implements MetricProvider
{
  private String last_name;
  private Collection<Metric> result;

  public FixedMetricProvider()
  {
    result = new ArrayList<Metric>();
  }

  public final Collection<? extends Metric> findByName (final String name)
  {
    last_name = name;
    return result;
  }

  public final String getLastName ()
  {
    return last_name;
  }

  public final void add (final Metric metric)
  {
    result.add(metric);
  }
}
