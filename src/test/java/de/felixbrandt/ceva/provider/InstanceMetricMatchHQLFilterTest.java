package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.MetricType;

public class InstanceMetricMatchHQLFilterTest
{
  InstanceMetricHQLFilter filter;

  @Before
  public void setup ()
  {
    InstanceMetric metric = new InstanceMetric();
    metric.setMetric(42);
    metric.setType(MetricType.STRING_METRIC);

    List<String> values = new ArrayList<String>();
    values.add("test");
    values.add("other");

    filter = new InstanceMetricMatchHQLFilter(metric, values);

  }

  @Test
  public void testQuery ()
  {
    assertEquals(
            " AND instance IN (SELECT d_1.source FROM InstanceDataString d_1 WHERE d_1.rule = 42 AND (true IS false OR d_1.value LIKE :value_0_1 OR d_1.value LIKE :value_1_1))",
            filter.getWhereClause("1"));
  }

  @Test
  public void testParameters ()
  {
    Map<String, Object> params = filter.getParameters("pref");
    assertEquals(2, params.size());
    assertEquals("%test%", params.get("value_0_pref"));
    assertEquals("%other%", params.get("value_1_pref"));
  }
}
