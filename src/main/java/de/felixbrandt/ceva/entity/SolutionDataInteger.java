package de.felixbrandt.ceva.entity;

/**
 * Integer result of a SolutionMetric execution.
 */
public class SolutionDataInteger extends DataInteger<SolutionMetric, Solution>
{
  private static final long serialVersionUID = 1L;

  public SolutionDataInteger()
  {
    super();
  }

  public SolutionDataInteger(final Solution solution, final SolutionMetric metric,
          final int version, final long value)
  {
    super(solution, metric, version, value);
  }
}
