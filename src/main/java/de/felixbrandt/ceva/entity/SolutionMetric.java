package de.felixbrandt.ceva.entity;

import de.felixbrandt.ceva.controller.base.ContentMode;

/**
 * Metric to calculation for a Solution.
 */
@SuppressWarnings("designforextension")
public class SolutionMetric extends Metric
{
  private static final long serialVersionUID = 1L;
  private ContentMode mode;

  public SolutionMetric()
  {
    super();
    init();
  }

  public SolutionMetric(final String name)
  {
    super(name);
    init();
  }

  private void init ()
  {
    setMode(ContentMode.DEFAULT);
  }

  public void setMode (final ContentMode _mode)
  {
    mode = _mode;
  }

  public ContentMode getMode ()
  {
    return mode;
  }

  @Override
  public final void updateMetricDetails (final Metric from_metric)
  {
    if (from_metric instanceof SolutionMetric) {
      final SolutionMetric from_solution_metric = (SolutionMetric) from_metric;
      setMode(from_solution_metric.getMode());
    }
  }
}
