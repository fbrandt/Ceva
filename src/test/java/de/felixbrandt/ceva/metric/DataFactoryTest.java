package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockCommand;
import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.metric.DataFactory;

public class DataFactoryTest
{
  class MockData extends Data<Metric, Object>
  {
    private static final long serialVersionUID = 1L;
    public Object input;
    public Object output;

    @Override
    public void setRawValue (final String input)
    {
      this.input = input;
    }

    @Override
    public Object getValue ()
    {
      return this.output;
    }
  };

  class MockDataFactory extends DataFactory<Metric, Object>
  {
    public MockData object;

    public MockDataFactory(final Metric metric)
    {
      super(metric);
      object = new MockData();
    }

    @Override
    public Data<Metric, Object> doCreateData (final Metric metric)
    {
      return object;
    }

  }

  MockDataFactory factory;
  InstanceMetric metric;

  @Before
  public void setup ()
  {
    metric = new InstanceMetric();
    metric.setName("TEST");
    metric.setType(MetricType.INT_METRIC);
    factory = new MockDataFactory(metric);
  }

  @Test
  public void testCreateResult ()
  {
    final MockCommand result = new MockCommand();
    factory.object.output = new Object();
    final Data<Metric, Object> data = factory.create(result);
    assertEquals(factory.object, data);
  }

  @Test
  public void testCreateResultFail ()
  {
    final MockCommand result = new MockCommand();
    factory.object.output = null;
    assertNull(factory.create(result));
  }
}
