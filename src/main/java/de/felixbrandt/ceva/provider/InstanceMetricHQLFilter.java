package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import de.felixbrandt.ceva.entity.InstanceMetric;

public abstract class InstanceMetricHQLFilter extends HQLFilter
{

  private InstanceMetric metric;
  private List<String> values;

  public InstanceMetricHQLFilter(InstanceMetric metric, List<String> values)
  {
    this.metric = metric;
    this.values = values;
  }

  public final InstanceMetric getMetric ()
  {
    return metric;
  }

  public final void setParametersToQuery (Query query, String prefix)
  {
    for (Map.Entry<String, Object> entry : getParameters(prefix).entrySet()) {
      if (entry.getValue() instanceof Collection) {
        query.setParameterList(entry.getKey(), (Collection) entry.getValue());
      } else {
        query.setParameter(entry.getKey(), entry.getValue());
      }
    }
  }

  public abstract Map<String, Object> getParameters (String prefix);

  public final List getValues ()
  {
    List result = values;

    switch (metric.getType()) {
    case INT_METRIC:
      result = new ArrayList<Integer>();
      for (String s : values) {
        result.add(Integer.valueOf(s));
      }
      return result;
    case DOUBLE_METRIC:
      result = new ArrayList<Double>();
      for (String s : values) {
        result.add(Double.valueOf(s));
      }
      return result;
    }

    return result;
  }

}
