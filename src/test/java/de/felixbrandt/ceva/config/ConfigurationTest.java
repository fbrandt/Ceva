package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.AlgorithmFactory;
import de.felixbrandt.ceva.entity.InstanceMetricFactory;
import de.felixbrandt.ceva.entity.SolutionMetricFactory;
import de.felixbrandt.support.ParameterMap;
import de.felixbrandt.support.StreamSupport;

public class ConfigurationTest
{

  Configuration config;

  @Before
  public void setup ()
  {
    config = new Configuration(StreamSupport.createEmptyInputStream());
  }

  @Test
  public void testDatabaseDefaults ()
  {
    assertEquals(0, config.getDatabaseParams().size());
  }

  @Test
  public void testDatabaseParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("database:\n  host: example.org");
    config = new Configuration(stream);

    final ParameterMap params = config.getDatabaseParams();
    assertEquals("example.org", params.getStringParam("host"));
  }

  @Test
  public void testDatabaseConfiguration ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("database:\n  host: example.org");
    config = new Configuration(stream);
    assertEquals("example.org", config.getDatabaseConfig().getHost());
  }

  @Test
  public void testQueueDefaults ()
  {
    assertEquals(0, config.getQueueParams().size());
  }

  @Test
  public void testQueueParams ()
  {
    final InputStream stream = StreamSupport.createInputStream("queue:\n  worker: 2");
    config = new Configuration(stream);

    final ParameterMap params = config.getQueueParams();
    assertEquals(2, params.getIntParam("worker"));
  }

  @Test
  public void testQueueConfiguration ()
  {
    final InputStream stream = StreamSupport.createInputStream("queue:\n  worker: 2");
    config = new Configuration(stream);
    assertEquals(2, config.getQueueConfig().getWorkerCount());
  }

  @Test
  public void testInstanceDefaults ()
  {
    assertEquals(0, config.getInstanceParams().size());
  }

  @Test
  public void testInstanceParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("instances:\n  - folder: instance\n  - file: instance.txt\n");
    config = new Configuration(stream);
    final List data = config.getInstanceParams();
    assertEquals(2, data.size());
  }

  @Test
  public void testInstanceConfiguration ()
  {
    final InstanceConfiguration instance_config = config.getInstanceConfig();
    assertTrue(instance_config != null);
  }

  @Test
  public void testInstanceMetricsDefaults ()
  {
    assertEquals(0, config.getInstanceMetricParams().size());
  }

  @Test
  public void testInstanceMetricsParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("imetrics:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final ParameterMap data = config.getInstanceMetricParams();
    assertEquals(1, data.size());
  }

  @Test
  public void testInstanceMetrics ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("imetrics:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final RuleConfiguration rule_config = config.getInstanceMetrics();
    assertEquals(RuleConfiguration.class, rule_config.getClass());
    assertEquals(1, rule_config.getRules().size());
    assertEquals(InstanceMetricFactory.class, rule_config.getRuleFactory().getClass());
  }

  @Test
  public void testAlgorithmParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("algorithms:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final ParameterMap data = config.getAlgorithmParams();
    assertEquals(1, data.size());
  }

  @Test
  public void testAlgorithms ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("algorithms:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final RuleConfiguration rule_config = config.getAlgorithms();
    assertEquals(RuleConfiguration.class, rule_config.getClass());
    assertEquals(1, rule_config.getRules().size());
    assertEquals(AlgorithmFactory.class, rule_config.getRuleFactory().getClass());
  }

  @Test
  public void testSMetricsParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("smetrics:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final ParameterMap data = config.getSolutionMetricParams();
    assertEquals(1, data.size());
  }

  @Test
  public void testOMetricsParams ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("ometrics:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final ParameterMap data = config.getSolutionMetricParams();
    assertEquals(1, data.size());
  }

  @Test
  public void testOMetrics ()
  {
    final InputStream stream = StreamSupport
            .createInputStream("ometrics:\n  rulename: \n    base_path: /\n");
    config = new Configuration(stream);
    final RuleConfiguration rule_config = config.getSolutionMetrics();
    assertEquals(RuleConfiguration.class, rule_config.getClass());
    assertEquals(1, rule_config.getRules().size());
    assertEquals(SolutionMetricFactory.class, rule_config.getRuleFactory().getClass());
  }

}
