package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceDataIntegerTest
{
  Instance instance;
  InstanceMetric metric;
  InstanceDataInteger data;

  @Before
  public void setup ()
  {
    instance = new Instance();
    metric = new InstanceMetric();
    data = new InstanceDataInteger();

    data.setSource(instance);
    data.setRule(metric);
    data.setVersion(23);
  }

  @Test
  public void testConstruct ()
  {
    assertEquals("instance initialized", instance, data.getSource());
    assertEquals("metric initialized", metric, data.getRule());
    assertEquals("version initialized", 23, data.getVersion());
  }

  @Test
  public void testValue ()
  {
    final InstanceDataInteger value = new InstanceDataInteger();
    value.setRawValue("42");
    assertEquals("value was set to integer 42", 42, value.getValue().longValue());
    value.setRawValue("23\n");
    assertEquals("setRawValue ignores line endings", 23, value.getValue().longValue());
  }

  @Test
  public void testValueBig ()
  {
    final InstanceDataInteger value = new InstanceDataInteger();
    value.setRawValue("3423432704");
    assertEquals("value was set to ", 3423432704L, value.getValue().longValue());
  }

  @Test
  public void testValueFail ()
  {
    final InstanceDataInteger value = new InstanceDataInteger();
    value.setRawValue("42 23");
    assertEquals("value was not set", null, value.getValue());
  }

  @Test
  public void testMatches ()
  {
    assertFalse(data.matches(null, null, 0));
    assertFalse(data.matches(instance, metric, 0));
    assertTrue(data.matches(instance, metric, 23));
  }

  @Test
  public void testSerialize () throws IOException
  {
    final ObjectOutputStream serializer = new ObjectOutputStream(new ByteArrayOutputStream());
    serializer.writeObject(data);
  }
}
