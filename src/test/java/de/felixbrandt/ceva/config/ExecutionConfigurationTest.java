package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import de.felixbrandt.support.ParameterMap;
import de.felixbrandt.support.StreamSupport;

public class ExecutionConfigurationTest
{
  Map<String, Object> data;
  ParameterMap params;
  ExecutionConfiguration config;

  @Before
  public void setUp () throws Exception
  {
    data = new HashMap<String, Object>();
    params = new ParameterMap(data);
    config = new ExecutionConfiguration();
  }

  @Test
  public void testYamlMapInList ()
  {
    final Yaml yaml = new Yaml();
    final InputStream stream = StreamSupport.createInputStream(
            "instances:\n  - host: example.org\n    port: 80\n  - host: other\n    port: 81");
    params = new ParameterMap((Map<String, ?>) yaml.load(stream));
    List<?> instances = params.getListParam("instances");
    assertEquals(2, instances.size());
    ParameterMap instance_params = new ParameterMap((Map<String, ?>) instances.get(0));
    assertTrue(instance_params.has("host"));
  }

  @Test
  public void testDefault ()
  {
    assertEquals(0, config.getInstanceFilters().size());
  }

  @Test
  public void testEmptyInstances ()
  {
    data.put("instances", null);
    assertEquals(0, config.getInstanceFilters().size());
  }

  @Test
  public void testInstances ()
  {
    List<String> filters = new ArrayList<String>();
    filters.add(null);
    filters.add(null);
    data.put("instances", filters);
    config = new ExecutionConfiguration(params);
    assertEquals(2, config.getInstanceFilters().size());
  }

  @Test
  public void testCreateFileInstanceFilter ()
  {
    List<String> filenames = new ArrayList<String>();
    filenames.add("test");
    filenames.add("foobar");
    data.put("files", filenames);
    InstanceFilterConfiguration filter = config.createInstanceFilter(params);
    assertTrue(filter instanceof FileInstanceFilterConfiguration);
    assertEquals("file", filter.getType());
    assertEquals(2, ((FileInstanceFilterConfiguration) filter).getFilenames().size());
  }

  @Test
  public void testCreateMetricValueInstanceFilter ()
  {
    data.put("metric", "mymetric");
    data.put("value", "valuetomatch");
    InstanceFilterConfiguration filter = config.createInstanceFilter(params);
    assertTrue(filter instanceof MetricValueInstanceFilterConfiguration);
    assertEquals("metricvalue", filter.getType());
    assertEquals("mymetric", ((MetricValueInstanceFilterConfiguration) filter).getMetric());
    assertEquals(1, ((MetricValueInstanceFilterConfiguration) filter).getValues().size());
    assertEquals("valuetomatch",
            ((MetricValueInstanceFilterConfiguration) filter).getValues().get(0));
  }

  @Test
  public void testCreateMetricValuesInstanceFilter ()
  {
    List values_list = new ArrayList();
    values_list.add("1");
    values_list.add(2);
    data.put("metric", "mymetric");
    data.put("values", values_list);
    InstanceFilterConfiguration filter = config.createInstanceFilter(params);
    assertTrue(filter instanceof MetricValueInstanceFilterConfiguration);
    assertEquals("metricvalue", filter.getType());
    assertEquals("mymetric", ((MetricValueInstanceFilterConfiguration) filter).getMetric());
    assertEquals(2, ((MetricValueInstanceFilterConfiguration) filter).getValues().size());
    assertEquals("1", ((MetricValueInstanceFilterConfiguration) filter).getValues().get(0));
    assertEquals("2", ((MetricValueInstanceFilterConfiguration) filter).getValues().get(1));
  }

  @Test
  public void testCreateMetricMatchInstanceFilter ()
  {
    data.put("metric", "mymetric");
    data.put("contains", "valuetomatch");
    InstanceFilterConfiguration filter = config.createInstanceFilter(params);
    assertTrue(filter instanceof MetricMatchInstanceFilterConfiguration);
    assertEquals("metricmatch", filter.getType());
    assertEquals("mymetric", ((MetricMatchInstanceFilterConfiguration) filter).getMetric());
    assertEquals(1, ((MetricMatchInstanceFilterConfiguration) filter).getValues().size());
    assertEquals("valuetomatch",
            ((MetricMatchInstanceFilterConfiguration) filter).getValues().get(0));
  }

  @Test
  public void testCreateMetricMatchMultiInstanceFilter ()
  {
    List values_list = new ArrayList();
    values_list.add("1");
    values_list.add(2);
    data.put("metric", "mymetric");
    data.put("contains", values_list);
    InstanceFilterConfiguration filter = config.createInstanceFilter(params);
    assertTrue(filter instanceof MetricMatchInstanceFilterConfiguration);
    assertEquals("metricmatch", filter.getType());
    assertEquals("mymetric", ((MetricMatchInstanceFilterConfiguration) filter).getMetric());
    assertEquals(2, ((MetricMatchInstanceFilterConfiguration) filter).getValues().size());
    assertEquals("1", ((MetricMatchInstanceFilterConfiguration) filter).getValues().get(0));
    assertEquals("2", ((MetricMatchInstanceFilterConfiguration) filter).getValues().get(1));
  }
}
