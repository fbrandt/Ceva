package de.felixbrandt.ceva;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.MetricMatchInstanceFilterConfiguration;
import de.felixbrandt.ceva.config.MetricValueInstanceFilterConfiguration;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.provider.InstanceMetricProvider;
import de.felixbrandt.support.ParameterMap;

public class InstanceFilterBuilderTest
{

  class MockInstanceMetricProvider implements InstanceMetricProvider
  {
    @Override
    public Collection<InstanceMetric> getMetrics ()
    {
      return null;
    }

    @Override
    public Collection<InstanceMetric> findByName (String name)
    {
      return new ArrayList<InstanceMetric>();
    }
  }

  InstanceFilterBuilder builder;

  @Before
  public void setup ()
  {
    builder = new InstanceFilterBuilder(new MockInstanceMetricProvider());
  }

  @Test
  public void testMatchNonexistent ()
  {
    assertNull(builder.buildMatchFilter(
            new MetricMatchInstanceFilterConfiguration(new ParameterMap())));
  }

  @Test
  public void testValueNonexistent ()
  {
    assertNull(builder.buildValueFilter(
            new MetricValueInstanceFilterConfiguration(new ParameterMap())));
  }
}
