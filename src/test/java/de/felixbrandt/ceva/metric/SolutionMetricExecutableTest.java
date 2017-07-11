package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.MetricExecutable;
import de.felixbrandt.ceva.metric.SolutionDataFactory;
import de.felixbrandt.ceva.metric.SolutionMetricExecutable;

public class SolutionMetricExecutableTest
{
  private SolutionMetric metric;
  private SolutionMetricExecutable executable;

  class TestMetric extends Metric
  {
    private static final long serialVersionUID = 1L;
  };

  @Before
  public void setUp () throws Exception
  {
    metric = new SolutionMetric();
    executable = new SolutionMetricExecutable(metric);
  }

  @Test
  public void testGetResultFactory ()
  {
    metric.setType(MetricType.INT_METRIC);
    final ResultFactory factory = executable.getResultFactory();
    assertTrue(factory instanceof SolutionDataFactory);

    final SolutionDataFactory my_factory = (SolutionDataFactory) factory;
    assertEquals(metric, my_factory.getMetric());
  }

  @Test
  public void testGenerate ()
  {
    final Vector<SolutionMetric> metrics = new Vector<SolutionMetric>();
    metrics.add(metric);
    final Collection<SolutionMetricExecutable> list = SolutionMetricExecutable
            .generate(metrics);
    assertEquals(1, list.size());
    final MetricExecutable item = list.iterator().next();
    assertEquals(metric, item.getMetric());
  }

  @Test
  public void testMode ()
  {
    metric.setMode(ContentMode.STDERR);
    assertEquals(ContentMode.STDERR, executable.getInputMode());
  }
}
