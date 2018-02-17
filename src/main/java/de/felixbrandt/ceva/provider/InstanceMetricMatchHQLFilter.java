package de.felixbrandt.ceva.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceMetricMatchHQLFilter extends InstanceMetricHQLFilter
{

  public InstanceMetricMatchHQLFilter(InstanceMetric metric, List<String> values)
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
    result.append(" AND (true IS false");

    for (int i = 0; i < getValues().size(); i++) {
      result.append(" OR " + sub_prefix + ".value LIKE :value_" + i + "_" + prefix);
    }

    result.append("))");
    return result.toString();
  }

  public final Map<String, Object> getParameters (String prefix)
  {
    Map<String, Object> result = new HashMap<String, Object>();

    List values = getValues();
    for (int i = 0; i < values.size(); i++) {
      result.put("value_" + i + "_" + prefix, "%" + values.get(i) + "%");
    }

    return result;
  }
}
