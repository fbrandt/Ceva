package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceDataString;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.provider.FixedDataProvider;
import de.felixbrandt.ceva.provider.FixedInstanceProvider;
import de.felixbrandt.ceva.provider.FixedMetricProvider;
import de.felixbrandt.ceva.report.InstanceDataReport;

public class InstanceDataReportTest
{

  ByteArrayOutputStream stream;
  InstanceDataReport report;
  InstanceDataInteger int_data;
  FixedInstanceProvider instance_provider;
  FixedMetricProvider metric_provider;
  FixedDataProvider data_provider;
  ArrayList<String> args;

  @Before
  public void setUp () throws Exception
  {
    final Instance instance = new Instance("INSTANCE");
    final InstanceMetric metric = new InstanceMetric("METRIC");
    int_data = new InstanceDataInteger(instance, metric, 0, 42);

    stream = new ByteArrayOutputStream();
    instance_provider = new FixedInstanceProvider();
    metric_provider = new FixedMetricProvider();
    data_provider = new FixedDataProvider(int_data);

    report = new InstanceDataReport(new PrintStream(stream), instance_provider,
            metric_provider, data_provider);

    args = new ArrayList<String>();
  }

  @Test
  public void testIntegerRun ()
  {
    instance_provider.add(new Instance());
    metric_provider.add(new InstanceMetric());

    report.run(args, 0);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)ID;INSTANCE;METRIC;VALUE(.*)"));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*);INSTANCE;METRIC;42(.*)"));
  }

  @Test
  public void testRetrieveInstances ()
  {
    args.add("instance");
    final Collection<?> result = report.retrieveSources(args, 0);
    assertEquals(0, result.size());
    assertEquals("instance", instance_provider.getLastKeyword());
  }

  @Test
  public void testRetrieveInstancesAll ()
  {
    final Collection<?> result = report.retrieveSources(args, 0);
    assertEquals(0, result.size());
    assertEquals("*", instance_provider.getLastKeyword());
  }

  @Test
  public void testRetrieveMetrics ()
  {
    args.add("instance");
    args.add("metric");
    final Collection<? extends Metric> result = report.retrieveMetrics(args, 0);
    assertEquals(0, result.size());
    assertEquals("metric", metric_provider.getLastName());
  }

  @Test
  public void testRetrieveMetricsAll ()
  {
    args.add("instance");
    final Collection<? extends Metric> result = report.retrieveMetrics(args, 0);
    assertEquals(0, result.size());
    assertEquals("all", metric_provider.getLastName());
  }

  @Test
  public void testRetrieveData ()
  {
    final Collection<InstanceMetric> metrics = new ArrayList<InstanceMetric>();
    metrics.add(new InstanceMetric());
    metrics.add(new InstanceMetric());

    final Collection<Instance> instances = new ArrayList<Instance>();
    instances.add(new Instance());
    instances.add(new Instance());

    final Collection<Data<?, ?>> result = report.retrieveData(instances, metrics);
    assertEquals(2, result.size());
    assertEquals(2, data_provider.getLastRules().size());
    assertEquals(metrics.iterator().next(), data_provider.getLastRules().iterator().next());
    assertEquals(instances, data_provider.getLastSources());
  }

  @Test
  public void testPrintListData ()
  {
    final Collection<Data<?, ?>> list = new ArrayList<Data<?, ?>>();
    list.add(int_data);
    list.add(int_data);
    report.print(list);

    final String result = stream.toString();
    assertFalse(result.equals(""));
    final int count = result.length() - result.replaceAll("4", "").length();
    assertEquals(result, 2, count);
  }

  @Test
  public void testPrintNull ()
  {
    report.print(null);
    assertEquals("", stream.toString());
  }

  @Test
  public void testPrintData ()
  {
    report.printRow(new PrintStream(stream), int_data);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*);INSTANCE;METRIC;42(.*)"));
  }

  @Test
  public void testPrintStringData ()
  {
    final Instance instance = new Instance("INSTANCE");
    final InstanceMetric metric = new InstanceMetric("METRIC");
    metric.setType(MetricType.STRING_METRIC);
    InstanceDataString data = new InstanceDataString(instance, metric, 0, "foobar");

    report.printRow(new PrintStream(stream), data);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*);INSTANCE;METRIC;foobar(.*)"));
  }

  @Test
  public void testParameterInstantiationWithJCommand ()
  {
    String[] parameters = { "-i", "instance", "-m", "metric" };
    new JCommander(report, parameters);

    assertEquals("instance", report.getInstanceKeyword());
    assertEquals("metric", report.getMetricName());
  }

}
