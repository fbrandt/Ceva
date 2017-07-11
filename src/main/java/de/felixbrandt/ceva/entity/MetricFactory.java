package de.felixbrandt.ceva.entity;

import java.util.HashMap;
import java.util.Map;

import de.felixbrandt.support.ParameterMap;

/**
 * Factory to setup Metric entities.
 */
public abstract class MetricFactory extends RuleFactory
{

  @Override
  public abstract Rule doCreate ();

  @Override
  public final void initRuleDetails (final Rule rule, final ParameterMap params)
  {
    if (rule instanceof Metric) {
      final Metric metric = (Metric) rule;
      initType(metric, params);
      initMetricDetails(metric, params);
    }
  }

  public final void initType (final Metric metric, final ParameterMap params)
  {
    MetricType type = MetricType.UNKNOWN_METRIC;
    Map<String, MetricType> types = new HashMap<String, MetricType>();
    types.put("integer", MetricType.INT_METRIC);
    types.put("float", MetricType.DOUBLE_METRIC);
    types.put("string", MetricType.STRING_METRIC);

    if (params == null) {
      type = MetricType.INT_METRIC;
    } else {
      String type_param = params.getStringParam("type", "integer");
      if (types.containsKey(type_param)) {
        type = types.get(type_param);
      } else {
        type = MetricType.UNKNOWN_METRIC;
      }
    }

    metric.setType(type);
  }

  public void initMetricDetails (final Metric rule, final ParameterMap params)
  {

  }
}
