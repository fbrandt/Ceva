package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.InstanceDataDouble;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceDataString;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.metric.InstanceDataFactory;

public class InstanceDataFactoryTest
{
  InstanceMetric metric;
  InstanceDataFactory factory;

  @Before
  public void setUp () throws Exception
  {
    metric = new InstanceMetric();
    factory = new InstanceDataFactory(metric);
  }

  @Test
  public void testCreateFail ()
  {
    metric.setType(MetricType.UNKNOWN_METRIC);
    assertNull(factory.createData(metric, null, 0));
  }

  @Test
  public void testDoCreateInteger ()
  {
    metric.setType(MetricType.INT_METRIC);
    assertEquals(InstanceDataInteger.class, factory.createData(metric, null, 0).getClass());
  }

  @Test
  public void testDoCreateString ()
  {
    metric.setType(MetricType.STRING_METRIC);
    assertEquals(InstanceDataString.class, factory.createData(metric, null, 0).getClass());
  }

  @Test
  public void testDoCreateDouble ()
  {
    metric.setType(MetricType.DOUBLE_METRIC);
    assertEquals(InstanceDataDouble.class, factory.createData(metric, null, 0).getClass());
  }
}
