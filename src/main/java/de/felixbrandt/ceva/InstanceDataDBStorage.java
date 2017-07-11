package de.felixbrandt.ceva;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Metric;

/**
 * Save results of instance metric calculations in database.
 */
public class InstanceDataDBStorage extends DataDBStorage
{

  public InstanceDataDBStorage(final SessionHandler handler)
  {
    super(handler);
  }

  @Override
  public final String getTablename (final Metric metric)
  {
    switch (metric.getType()) {
    case STRING_METRIC:
      return "InstanceDataString";
    case DOUBLE_METRIC:
      return "InstanceDataDouble";
    case INT_METRIC:
    default:
      return "InstanceDataInteger";
    }
  }

  @Override
  public final String getSourceObjectName ()
  {
    return "Instance";
  }

  @Override
  public final String getSourceIDName ()
  {
    return "instance";
  }

}
