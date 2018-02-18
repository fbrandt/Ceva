package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.InstanceMetric;

public class MetricExecutableTest
{
  private InstanceMetric metric;
  private InstanceMetricExecutable executable;

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
  public void testDefaultParameters ()
  {
    assertEquals(0, executable.getParameters().size());
  }

  @Test
  public void testParametersAsString ()
  {
    assertEquals("", executable.getParametersAsString());
  }

  @Test
  public void testGetFullVersionPath ()
  {
    metric.setVersionPath("VERSIONPATH");
    assertEquals("VERSIONPATH", executable.getFullVersionPath());
  }

  @Test
  public void testGetFullRunPath ()
  {
    metric.setRunPath("RUNPATH");
    assertEquals("RUNPATH", executable.getFullRunPath());
  }

  @Test
  public void testToString ()
  {
    metric.setName("test");
    assertEquals("metric test", executable.toString());
  }

  @Test
  public void testGetName ()
  {
    metric.setName("test");
    assertEquals("test", executable.getName());
  }
}
