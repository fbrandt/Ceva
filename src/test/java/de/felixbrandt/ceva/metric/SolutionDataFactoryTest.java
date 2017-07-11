package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.SolutionDataDouble;
import de.felixbrandt.ceva.entity.SolutionDataInteger;
import de.felixbrandt.ceva.entity.SolutionDataString;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.SolutionDataFactory;

public class SolutionDataFactoryTest
{
  SolutionMetric metric;
  SolutionDataFactory factory;

  @Before
  public void setUp () throws Exception
  {
    metric = new SolutionMetric();
    factory = new SolutionDataFactory(metric);
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
    assertEquals(SolutionDataInteger.class, factory.createData(metric, null, 0).getClass());
  }

  @Test
  public void testDoCreateString ()
  {
    metric.setType(MetricType.STRING_METRIC);
    assertEquals(SolutionDataString.class, factory.createData(metric, null, 0).getClass());
  }

  @Test
  public void testDoCreateDouble ()
  {
    metric.setType(MetricType.DOUBLE_METRIC);
    assertEquals(SolutionDataDouble.class, factory.createData(metric, null, 0).getClass());
  }
}
