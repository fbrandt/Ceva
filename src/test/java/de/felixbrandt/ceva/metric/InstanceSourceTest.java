package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.support.StreamSupport;

public class InstanceSourceTest
{
  private Instance instance;
  private InstanceSource source;

  @Before
  public void setup ()
  {
    instance = new Instance();
    source = new InstanceSource(instance);
  }

  @Test
  public void testGetName ()
  {
    instance.setName("filename");
    assertEquals("filename", source.getName());
  }

  @Test
  public void testGetContent ()
  {
    instance.setContent("content");
    assertEquals("content", StreamSupport.getStringFromInputStream(source.getContent()));
  }

}
