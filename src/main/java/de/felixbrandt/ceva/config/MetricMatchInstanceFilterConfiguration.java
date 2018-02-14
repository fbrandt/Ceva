package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

public class MetricMatchInstanceFilterConfiguration extends MetricInstanceFilterConfiguration
{

  public MetricMatchInstanceFilterConfiguration(ParameterMap params)
  {
    super(params, "contains");
  }

  public String getType ()
  {
    return "metricmatch";
  }

}
