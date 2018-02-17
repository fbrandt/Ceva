package de.felixbrandt.ceva.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceMetricValueHQLFilter extends InstanceMetricHQLFilter
{
  public InstanceMetricValueHQLFilter(InstanceMetric metric, List<String> values)
  {
    super(metric, values);
  }

  public final String getWhereClause (String prefix)
  {
    final String sub_prefix = "d_" + prefix;
    final StringBuilder result = new StringBuilder();

    result.append("instance IN (SELECT " + sub_prefix + ".source");
    result.append(" FROM " + getMetric().getDataEntity() + " " + sub_prefix);
    result.append(" WHERE " + sub_prefix + ".rule = " + getMetric().getId());
    result.append(" AND " + sub_prefix + ".value IN :value_" + prefix + ")");

    return result.toString();
  }

  public final Map<String, Object> getParameters (String prefix)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("value_" + prefix, getValues());
    return result;
  }

}
