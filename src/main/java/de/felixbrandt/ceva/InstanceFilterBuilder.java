package de.felixbrandt.ceva;

import java.util.ArrayList;
import java.util.List;

import de.felixbrandt.ceva.config.InstanceFilterConfiguration;
import de.felixbrandt.ceva.config.MetricMatchInstanceFilterConfiguration;
import de.felixbrandt.ceva.config.MetricValueInstanceFilterConfiguration;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.provider.HQLFilter;
import de.felixbrandt.ceva.provider.InstanceMetricDBProvider;
import de.felixbrandt.ceva.provider.InstanceMetricMatchHQLFilter;
import de.felixbrandt.ceva.provider.InstanceMetricValueHQLFilter;

/**
 * Build HQLFilters according to InstanceFilterConfiguration.
 */
public class InstanceFilterBuilder
{
  private InstanceMetricDBProvider instance_metric_provider;

  public InstanceFilterBuilder(InstanceMetricDBProvider provider)
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

  private HQLFilter buildMatchFilter (MetricMatchInstanceFilterConfiguration config)
  {
    InstanceMetric metric = instance_metric_provider.findByName(config.getMetric()).iterator()
            .next();
    return new InstanceMetricMatchHQLFilter(metric, config.getValues());
  }

  private InstanceMetricValueHQLFilter buildValueFilter (
          MetricValueInstanceFilterConfiguration config)
  {
    InstanceMetric metric = instance_metric_provider.findByName(config.getMetric()).iterator()
            .next();
    return new InstanceMetricValueHQLFilter(metric, config.getValues());
  }
}
