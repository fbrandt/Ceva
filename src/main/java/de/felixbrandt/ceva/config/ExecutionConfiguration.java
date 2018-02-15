package de.felixbrandt.ceva.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.felixbrandt.support.ParameterMap;

/**
 * Configure what to do on the current CEVA run.
 */
public class ExecutionConfiguration
{
  private ParameterMap params;

  public ExecutionConfiguration()
  {
    init(new ParameterMap(null));
  }

  public ExecutionConfiguration(final ParameterMap params)
  {
    init(params);
  }

  private final void init (final ParameterMap params)
  {
    this.params = params;
  }

  public final List<InstanceFilterConfiguration> createInstanceFilters (final List<?> filters)
  {
    List<InstanceFilterConfiguration> instance_filters = new ArrayList<InstanceFilterConfiguration>();

    for (Object filter : filters) {
      if (filter != null) {
        final ParameterMap params = new ParameterMap((Map<String, ?>) filter);
        instance_filters.add(createInstanceFilter(params));
      }
    }

    return instance_filters;
  }

  public final InstanceFilterConfiguration createInstanceFilter (final ParameterMap params)
  {
    if (params.has("files")) {
      return new FileInstanceFilterConfiguration((List<String>) params.getListParam("files"));
    } else if (params.has("metric")) {
      if (params.has("contains")) {
        return new MetricMatchInstanceFilterConfiguration(params);
      }
      return new MetricValueInstanceFilterConfiguration(params);
    }

    return null;
  }

  public final List<InstanceFilterConfiguration> getInstanceFilters ()
  {
    return createInstanceFilters(params.getListParam("instances"));
  }

  public final RuleExecutionConfiguration getRuleConfiguration (final ParameterMap params,
          String key)
  {
    if (params.isMapParam(key)) {
      return new RuleExecutionConfiguration(true, params.getMapParam(key));
    }

    boolean active = params.getBoolParam(key, true);
    return new RuleExecutionConfiguration(active);
  }

  public RuleExecutionConfiguration getInstanceMetricConfiguration ()
  {
    return getRuleConfiguration(params, "imetrics");
  }

  public RuleExecutionConfiguration getAlgorithmConfiguration ()
  {
    return getRuleConfiguration(params, "algorithms");
  }

  public RuleExecutionConfiguration getSolutionMetricConfiguration ()
  {
    return getRuleConfiguration(params, "smetrics");
  }
}
