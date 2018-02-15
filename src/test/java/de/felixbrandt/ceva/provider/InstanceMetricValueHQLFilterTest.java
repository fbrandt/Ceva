package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.MetricType;

public class InstanceMetricValueHQLFilterTest
{
  InstanceMetric metric;
  List<String> values;

  @Before
  public void setup ()
  {
    metric = new InstanceMetric();
    metric.setMetric(42);
    values = new ArrayList<String>();
  }

  @Test
  public void testStringMetric ()
  {
    metric.setType(MetricType.STRING_METRIC);
    values.add("test");
    InstanceMetricValueHQLFilter filter = new InstanceMetricValueHQLFilter(metric, values);

    assertEquals(
            " AND instance IN (SELECT d_1.source FROM InstanceDataString d_1 WHERE d_1.rule = 42 AND d_1.value IN :value_1)",
            filter.getWhereClause("1"));

    Map<String, Object> params = filter.getParameters("prefix");
    assertEquals(1, params.size());
    assertEquals(values, params.get("value_prefix"));
  }

  @Test
  public void testIntegerMetric ()
  {
    metric.setType(MetricType.INT_METRIC);
    values.add("123");
    List<Integer> int_values = new ArrayList<Integer>();
    int_values.add(123);

    InstanceMetricValueHQLFilter filter = new InstanceMetricValueHQLFilter(metric, values);

    assertEquals(
            " AND instance IN (SELECT d_1.source FROM InstanceDataInteger d_1 WHERE d_1.rule = 42 AND d_1.value IN :value_1)",
            filter.getWhereClause("1"));
    assertEquals(int_values, filter.getValues());
  }

  @Test
  public void testDoubleMetric ()
  {
    metric.setType(MetricType.DOUBLE_METRIC);
    values.add("3.14");
    List<Double> dbl_values = new ArrayList<Double>();
    dbl_values.add(3.14);

    InstanceMetricValueHQLFilter filter = new InstanceMetricValueHQLFilter(metric, values);

    assertEquals(
            " AND instance IN (SELECT d_1.source FROM InstanceDataDouble d_1 WHERE d_1.rule = 42 AND d_1.value IN :value_1)",
            filter.getWhereClause("1"));
    assertEquals(dbl_values, filter.getValues());
  }
}
