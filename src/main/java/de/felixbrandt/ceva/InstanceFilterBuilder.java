package de.felixbrandt.ceva;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.config.InstanceFilterConfiguration;
import de.felixbrandt.ceva.config.MetricMatchInstanceFilterConfiguration;
import de.felixbrandt.ceva.config.MetricValueInstanceFilterConfiguration;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.provider.HQLFilter;
import de.felixbrandt.ceva.provider.InstanceMetricMatchHQLFilter;
import de.felixbrandt.ceva.provider.InstanceMetricProvider;
import de.felixbrandt.ceva.provider.InstanceMetricValueHQLFilter;

/**
 * Build HQLFilters according to InstanceFilterConfiguration.
 */
public class InstanceFilterBuilder
{
  private static final Logger LOGGER = LogManager.getLogger();
  private InstanceMetricProvider instance_metric_provider;

  public InstanceFilterBuilder(InstanceMetricProvider provider)
  {
    instance_metric_provider = provider;
  }

  public List<HQLFilter> build (List<InstanceFilterConfiguration> configs)
  {
    List<HQLFilter> filters = new ArrayList<HQLFilter>();

    for (InstanceFilterConfiguration config : configs) {
      filters.add(build(config));
    }

    return filters;
  }

  public HQLFilter build (InstanceFilterConfiguration config)
  {
    if (config.getType().equals("metricvalue")) {
      return buildValueFilter((MetricValueInstanceFilterConfiguration) config);
    } else if (config.getType().equals("metricmatch")) {
      return buildMatchFilter((MetricMatchInstanceFilterConfiguration) config);
    }
    return null;
  }

  public HQLFilter buildMatchFilter (
          MetricMatchInstanceFilterConfiguration config)
  {
    InstanceMetric metric = getMetric(config.getMetric());
    if (metric == null) {
      return null;
    }

    return new InstanceMetricMatchHQLFilter(metric, config.getValues());
  }

  public InstanceMetricValueHQLFilter buildValueFilter (
          MetricValueInstanceFilterConfiguration config)
  {
    InstanceMetric metric = getMetric(config.getMetric());
    if (metric == null) {
      return null;
    }

    return new InstanceMetricValueHQLFilter(metric, config.getValues());
  }

  public InstanceMetric getMetric (String name)
  {
    Collection<InstanceMetric> result = instance_metric_provider
            .findByName(name);

    if (result.size() > 0) {
      return result.iterator().next();
    }

    LOGGER.error("Metric \"{}\" could not be found", name);
    return null;
  }
}
