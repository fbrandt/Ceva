package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.InstanceMetricFactory;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.support.ParameterMap;

public class InstanceMetricFactoryTest
{
  InstanceMetricFactory factory;

  @Before
  public void setUp () throws Exception
  {
    factory = new InstanceMetricFactory();
  }

  @Test
  public void testDoCreate ()
  {
    assertEquals(InstanceMetric.class, factory.doCreate().getClass());
  }

  @Test
  public void testInitParamsIsNull ()
  {
    final InstanceMetric base_metric = new InstanceMetric();
    final Rule final_metric = factory.init(base_metric, null);
    assertEquals(base_metric, final_metric);
    assertEquals(MetricType.INT_METRIC, ((Metric) final_metric).getType());
  }

  @Test
  public void testInitInteger ()
  {
    final HashMap<String, String> data = new HashMap<String, String>();
    data.put("type", "integer");
    final ParameterMap params = new ParameterMap(data);

    final InstanceMetric base_metric = new InstanceMetric();
    final Rule final_metric = factory.init(base_metric, params);
    assertEquals(base_metric, final_metric);
    assertEquals(MetricType.INT_METRIC, ((Metric) final_metric).getType());
  }

  @Test
  public void testInitString ()
  {
    final HashMap<String, String> data = new HashMap<String, String>();
    data.put("type", "string");
    final ParameterMap params = new ParameterMap(data);

    final InstanceMetric base_metric = new InstanceMetric();
    final Rule final_metric = factory.init(base_metric, params);
    assertEquals(base_metric, final_metric);
    assertEquals(MetricType.STRING_METRIC, ((Metric) final_metric).getType());
  }

  @Test
  public void testInitDouble ()
  {
    final HashMap<String, String> data = new HashMap<String, String>();
    data.put("type", "float");
    final ParameterMap params = new ParameterMap(data);

    final InstanceMetric base_metric = new InstanceMetric();
    final Rule final_metric = factory.init(base_metric, params);
    assertEquals(base_metric, final_metric);
    assertEquals(MetricType.DOUBLE_METRIC, ((Metric) final_metric).getType());
  }
}
