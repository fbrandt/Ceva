package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceMetricValueHQLFilter extends HQLFilter
{
  private InstanceMetric metric;
  private List<String> values;

  public InstanceMetricValueHQLFilter(InstanceMetric metric, List<String> values)
  {
    this.metric = metric;
    this.values = values;
  }

  public String getWhereClause (String prefix)
  {
    String sub_prefix = "d_" + prefix;
    return " AND instance IN (SELECT " + sub_prefix + ".source FROM " + metric.getDataEntity()
            + " " + sub_prefix + " WHERE " + sub_prefix + ".rule = " + metric.getMetric()
            + " AND " + sub_prefix + ".value IN :value_" + prefix + ")";
  }

  public void setParameters (Query query, String prefix)
  {
    query.setParameterList("value_" + prefix, getValues());
  }

  public List getValues ()
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
