package de.felixbrandt.ceva.config;

import java.util.ArrayList;
import java.util.List;

import de.felixbrandt.support.ParameterMap;

public abstract class MetricInstanceFilterConfiguration implements InstanceFilterConfiguration
{

  private String metric;
  private List<String> values;

  public MetricInstanceFilterConfiguration(ParameterMap params, String value_attribute)
  {
    metric = params.getStringParam("metric", "");

    values = new ArrayList<String>();
    for (Object value : params.getListParam(value_attribute)) {
      values.add(String.valueOf(value));
    }
  }

  public void addValue (String value)
  {
    values.add(value);
  }

  public String getMetric ()
  {
    return metric;
  }

  public List<String> getValues ()
  {
    return values;
  }

}
