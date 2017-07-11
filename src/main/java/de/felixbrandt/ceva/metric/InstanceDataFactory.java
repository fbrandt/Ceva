package de.felixbrandt.ceva.metric;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataDouble;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceDataString;
import de.felixbrandt.ceva.entity.InstanceMetric;

/**
 * Factory to create result of InstanceMetric calculation.
 */
public class InstanceDataFactory extends DataFactory<InstanceMetric, Instance>
{

  public InstanceDataFactory(final InstanceMetric metric)
  {
    super(metric);
  }

  @Override
  public final Data<InstanceMetric, Instance> doCreateData (final InstanceMetric metric)
  {
    switch (metric.getType()) {
    case INT_METRIC:
      return new InstanceDataInteger();
    case DOUBLE_METRIC:
      return new InstanceDataDouble();
    case STRING_METRIC:
      return new InstanceDataString();
    default:
      return unkownType(metric);
    }
  }
}
