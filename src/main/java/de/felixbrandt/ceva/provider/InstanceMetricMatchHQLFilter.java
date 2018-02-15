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
    String query = " AND instance IN (SELECT " + sub_prefix + ".source FROM "
            + getMetric().getDataEntity() + " " + sub_prefix + " WHERE " + sub_prefix
            + ".rule = " + getMetric().getId() + " AND (true IS false";

    for (int i = 0; i < getValues().size(); i++) {
      query = query + " OR " + sub_prefix + ".value LIKE :value_" + i + "_" + prefix;
    }

    query = query + "))";

    return query;
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
