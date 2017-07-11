package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.entity.SolutionMetricFactory;
import de.felixbrandt.support.ParameterMap;

public class SolutionMetricFactoryTest
{
  SolutionMetricFactory factory;

  @Before
  public void setUp () throws Exception
  {
    factory = new SolutionMetricFactory();
  }

  @Test
  public void testDoCreate ()
  {
    assertEquals(SolutionMetric.class, factory.doCreate().getClass());
  }

  @Test
  public void testInit ()
  {
    final SolutionMetric base_metric = new SolutionMetric();
    final Rule final_metric = factory.init(base_metric);
    assertEquals(base_metric, final_metric);
    assertEquals(ContentMode.DEFAULT, base_metric.getMode());
  }

  @Test
  public void testInitStderr ()
  {
    final HashMap<String, String> data = new HashMap<String, String>();
    data.put("input", "stderr");
    final ParameterMap params = new ParameterMap(data);

    final SolutionMetric base_metric = new SolutionMetric();
    factory.init(base_metric, params);
    assertEquals(ContentMode.STDERR, base_metric.getMode());
  }

  @Test
  public void testInitMetricDetails ()
  {
    Metric metric = new InstanceMetric();
    ParameterMap params = new ParameterMap();

    factory.initMetricDetails(metric, params);
  }

  @Test
  public void testInitModeParamsNull ()
  {
    SolutionMetric metric = new SolutionMetric();

    metric.setMode(ContentMode.DEFAULT);
    assertEquals(ContentMode.DEFAULT, metric.getMode());

    factory.initMode(metric, null);
    assertEquals(ContentMode.DEFAULT, metric.getMode());
  }

  @Test
  public void testInitModeParamsStderr ()
  {
    SolutionMetric metric = new SolutionMetric();
    metric.setMode(ContentMode.DEFAULT);

    Map<String, String> data = new HashMap<String, String>();
    data.put("input", "stderr");
    ParameterMap params = new ParameterMap(data);

    assertEquals(ContentMode.DEFAULT, metric.getMode());
    factory.initMode(metric, params);
    assertEquals(ContentMode.STDERR, metric.getMode());
  }

  @Test
  public void testInitModeParamsNoInputValue ()
  {
    SolutionMetric metric = new SolutionMetric();
    metric.setMode(ContentMode.DEFAULT);

    Map<String, String> data = new HashMap<String, String>();
    ParameterMap params = new ParameterMap(data);

    assertEquals(ContentMode.DEFAULT, metric.getMode());
    factory.initMode(metric, params);
    assertEquals(ContentMode.DEFAULT, metric.getMode());
  }
}
