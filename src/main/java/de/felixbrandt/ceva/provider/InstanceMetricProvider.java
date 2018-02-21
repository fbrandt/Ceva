package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.entity.InstanceMetric;

public interface InstanceMetricProvider
{
  public Collection<InstanceMetric> getMetrics ();

  public Collection<InstanceMetric> findByName (final String name);

}
