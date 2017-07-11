package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InstanceConfigurationTest
{

  InstanceConfiguration config;

  @Before
  public void setup ()
  {
    config = new InstanceConfiguration();
  }

  @Test
  public void testGetInstanceFile ()
  {
    final List<InstanceFile> files = config.getInstanceFolder(
            "src/test/java/de/felixbrandt/ceva/config/InstanceConfigurationTest.java");
    assertEquals(files.size(), 1);
    assertTrue(files.get(0).isReadable());
  }

  @Test
  public void testGetInstanceFolder ()
  {
    final List<InstanceFile> files = config
            .getInstanceFolder("src/test/resources/InstanceConfiguration");
    assertEquals(3, files.size());
  }

  @Test
  public void testComplex ()
  {
    final ArrayList list = new ArrayList();
    list.add("src/test/resources/InstanceConfiguration/test1.txt");
    list.add("src/test/resources/InstanceConfiguration");
    config = new InstanceConfiguration(list);
    assertEquals(4, config.getInstances().size());
  }
}
