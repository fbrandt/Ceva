package de.felixbrandt.ceva;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Metric;

/**
 * Save results of solution metric calculations in database.
 */
public class SolutionDataDBStorage extends DataDBStorage
{

  public SolutionDataDBStorage(final SessionHandler handler)
  {
    super(handler);
  }

  /**
   * Seems to be a shortcoming of Hibernate, that deserialized solutiondata objects throw a
   * PropertyAccessException, if only the parent table Data is queried.
   */
  @Override
  public final String getTablename (final Metric metric)
  {
    switch (metric.getType()) {
    case STRING_METRIC:
      return "SolutionDataString";
    case DOUBLE_METRIC:
      return "SolutionDataDouble";
    case INT_METRIC:
    default:
      return "SolutionDataInteger";
    }
  }

  @Override
  public final String getSourceObjectName ()
  {
    return "Solution";
  }

  @Override
  public final String getSourceIDName ()
  {
    return "solution";
  }

}
