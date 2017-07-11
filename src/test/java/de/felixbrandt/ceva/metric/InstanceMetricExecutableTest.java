package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.metric.InstanceDataFactory;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;

public class InstanceMetricExecutableTest
{
  private InstanceMetric metric;
  private InstanceMetricExecutable executable;

  class TestMetric extends Metric
  {
    private static final long serialVersionUID = 1L;
  };

  @Before
  public void setUp () throws Exception
  {
    metric = new InstanceMetric();
    executable = new InstanceMetricExecutable(metric);
  }

  @Test
  public void testDefaultRepeat ()
  {
    assertEquals(1, executable.getRepeat());
  }

  @Test
  public void testGetResultFactory ()
  {
    metric.setType(MetricType.INT_METRIC);
    final ResultFactory factory = executable.getResultFactory();
    assertTrue(factory instanceof InstanceDataFactory);

    final InstanceDataFactory my_factory = (InstanceDataFactory) factory;
    assertEquals(metric, my_factory.getMetric());
  }

  @Test
  public void testGenerate ()
  {
    final Vector<InstanceMetric> metrics = new Vector<InstanceMetric>();
    metrics.add(metric);
    final Collection<InstanceMetricExecutable> list = InstanceMetricExecutable
            .generate(metrics);
    assertEquals(1, list.size());
    final InstanceMetricExecutable item = list.iterator().next();
    assertEquals(metric, item.getMetric());
  }
}
