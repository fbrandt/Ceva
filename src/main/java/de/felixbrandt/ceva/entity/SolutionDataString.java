package de.felixbrandt.ceva.entity;

/**
 * Integer result of a SolutionMetric execution.
 */
public class SolutionDataString extends DataString<SolutionMetric, Solution>
{
  private static final long serialVersionUID = 1L;

  public SolutionDataString()
  {
    super();
  }

  public SolutionDataString(final Solution solution, final SolutionMetric metric,
          final int version, final String value)
  {
    super(solution, metric, version, value);
  }
}
