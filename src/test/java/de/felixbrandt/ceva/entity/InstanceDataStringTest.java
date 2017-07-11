package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataString;
import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceDataStringTest
{
  Instance instance;
  InstanceMetric metric;
  InstanceDataString data;

  @Before
  public void setup ()
  {
    instance = new Instance();
    metric = new InstanceMetric();
    data = new InstanceDataString();

    data.setSource(instance);
    data.setRule(metric);
    data.setVersion(23);
  }

  @Test
  public void testValue ()
  {
    final InstanceDataString value = new InstanceDataString();
    value.setRawValue("foobar");
    assertEquals("value was set to string", "foobar", value.getValue());
    value.setRawValue("foobar\n");
    assertEquals("setRawValue trims line endings", "foobar", value.getValue());
  }

  @Test
  public void testSerialize () throws IOException
  {
    final ObjectOutputStream serializer = new ObjectOutputStream(new ByteArrayOutputStream());
    serializer.writeObject(data);
  }
}
