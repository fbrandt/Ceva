package de.felixbrandt.ceva.entity;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.support.ParameterMap;

/**
 * Factory to setup SolutionMetric entities.
 */
public class SolutionMetricFactory extends MetricFactory
{

  @Override
  public final Rule doCreate ()
  {
    return new SolutionMetric();
  }

  @Override
  public final void initMetricDetails (final Metric metric, final ParameterMap params)
  {
    if (metric instanceof SolutionMetric) {
      final SolutionMetric solution_metric = (SolutionMetric) metric;
      initMode(solution_metric, params);
    }
  }

  public final void initMode (final SolutionMetric metric, final ParameterMap params)
  {
    if (params != null && params.getStringParam("input", "default").equals("stderr")) {
      metric.setMode(ContentMode.STDERR);
    }
  }
}
