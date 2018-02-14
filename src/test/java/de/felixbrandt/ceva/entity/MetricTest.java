package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MetricTest
{
  Metric metric;

  @SuppressWarnings("serial")
  class TestMetric extends Metric
  {
  };

  @Before
  public void Setup ()
  {
    metric = new TestMetric();
  }

  @Test
  public void testEmptyBasePath ()
  {
    assertEquals("base_path defaults to empty string", "", metric.getBasePath());
  }

  @Test
  public void testBasePath ()
  {
    metric.setBasePath("base");
    assertEquals("base_path defaults to empty string", "base", metric.getBasePath());
  }

  @Test
  public void testEmptyVersionPath ()
  {
    assertEquals("getVersionPath() defaults to empty string", "", metric.getVersionPath());
    assertEquals("getFullVersionPath() defaults to empty string", "",
            metric.getFullVersionPath());

  }

  @Test
  public void testFullVersionPathBase ()
  {
    metric.setBasePath("base");
    assertEquals("getFullVersionPath() contains base_path", "base",
            metric.getFullVersionPath());
  }

  @Test
  public void testFullVersionPathVersion ()
  {
    metric.setVersionPath("version");
    assertEquals("getFullVersionPath() contains version_path", "version",
            metric.getFullVersionPath());
  }

  @Test
  public void testFullVersionPath ()
  {
    metric.setVersionPath("version");
    metric.setBasePath("base");
    assertEquals("getFullVersionPath() adds space between base_path and version_path",
            "base version", metric.getFullVersionPath());
  }

  @Test
  public void testEmptyRunPath ()
  {
    assertEquals("getRunPath() defaults to empty string", "", metric.getRunPath());
    assertEquals("getFullRunPath() defaults to empty string", "", metric.getFullRunPath());

  }

  @Test
  public void testFullRunPathBase ()
  {
    metric.setBasePath("base");
    assertEquals("getFullRunPath() contains base_path", "base", metric.getFullRunPath());
  }

  @Test
  public void testFullRunPathVersion ()
  {
    metric.setRunPath("exec");
    assertEquals("getFullRunPath() contains run_path", "exec", metric.getFullRunPath());
  }

  @Test
  public void testFullRunPath ()
  {
    metric.setRunPath("exec");
    metric.setBasePath("base");
    assertEquals("getFullRunPath() adds space between base_path and run_path", "base exec",
            metric.getFullRunPath());
  }

  @Test
  public void testUpdateFrom ()
  {
    final TestMetric update = new TestMetric();
    update.setType(MetricType.INT_METRIC);

    final Rule returned = metric.updateFrom(update);
    assertEquals(metric, returned);

    assertEquals(MetricType.INT_METRIC, metric.getType());
  }

  @Test
  public void testSetAndGetMetric ()
  {
    assertEquals(MetricType.UNKNOWN_METRIC, metric.getType());
    metric.setType(MetricType.DOUBLE_METRIC);
    assertEquals(MetricType.DOUBLE_METRIC, metric.getType());
  }

  @Test
  public void testUpdateRuleDetails ()
  {
    Rule algo = new Algorithm();

    Metric new_metric = new TestMetric();
    new_metric.setType(MetricType.DOUBLE_METRIC);

    assertEquals(MetricType.UNKNOWN_METRIC, metric.getType());

    metric.updateFrom(algo);
    assertEquals(MetricType.UNKNOWN_METRIC, metric.getType());

    metric.updateFrom(new_metric);
    assertEquals(MetricType.DOUBLE_METRIC, metric.getType());
  }

  @Test
  public void testGetParameters ()
  {
    assertEquals(new HashMap<String, List<String>>(), metric.getParameters());
  }

  @Test
  public void getMetric ()
  {
    assertEquals(0, metric.getMetric());
  }

  @Test
  public void setMetric ()
  {
    assertEquals(0, metric.getMetric());
    metric.setMetric(1);
    assertEquals(1, metric.getMetric());
  }
}
