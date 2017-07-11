package de.felixbrandt.ceva.metric;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.entity.SolutionDataDouble;
import de.felixbrandt.ceva.entity.SolutionDataInteger;
import de.felixbrandt.ceva.entity.SolutionDataString;
import de.felixbrandt.ceva.entity.SolutionMetric;

/**
 * Factory to create result of SolutionMetric calculation.
 */
public class SolutionDataFactory extends DataFactory<SolutionMetric, Solution>
{
  public SolutionDataFactory(final SolutionMetric _metric)
  {
    super(_metric);
  }

  @Override
  public final Data<SolutionMetric, Solution> doCreateData (final SolutionMetric metric)
  {
    switch (metric.getType()) {
    case INT_METRIC:
      return new SolutionDataInteger();
    case DOUBLE_METRIC:
      return new SolutionDataDouble();
    case STRING_METRIC:
      return new SolutionDataString();
    default:
      return unkownType(metric);
    }
  }
}
