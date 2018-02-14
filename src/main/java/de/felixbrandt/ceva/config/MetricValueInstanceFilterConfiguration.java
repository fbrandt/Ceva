package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

public class MetricValueInstanceFilterConfiguration extends MetricInstanceFilterConfiguration
{
  public MetricValueInstanceFilterConfiguration(ParameterMap params)
  {
    super(params, "values");

    if (params.has("value")) {
      addValue(params.getStringParam("value"));
    }
  }

  public String getType ()
  {
    return "metricvalue";
  }
}
