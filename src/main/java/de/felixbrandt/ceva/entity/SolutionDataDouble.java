package de.felixbrandt.ceva.entity;

/**
 * Integer result of a SolutionMetric execution.
 */
public class SolutionDataDouble extends DataDouble<SolutionMetric, Solution>
{
  private static final long serialVersionUID = 1L;

  public SolutionDataDouble()
  {
    super();
  }

  public SolutionDataDouble(final Solution solution, final SolutionMetric metric,
          final int version, final long value)
  {
    super(solution, metric, version, value);
  }
}
