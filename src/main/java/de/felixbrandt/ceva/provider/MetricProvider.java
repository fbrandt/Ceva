package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.entity.Metric;

/**
 * Lookup instance metrics.
 */
public interface MetricProvider
{
  Collection<? extends Metric> findByName (final String name);
}
