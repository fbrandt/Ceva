package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.SolutionMetric;

public class SolutionMetricTest
{
  SolutionMetric metric;

  @Before
  public void Setup ()
  {
    metric = new SolutionMetric();
  }

  @Test
  public void testUpdateFrom ()
  {
    final SolutionMetric update = new SolutionMetric();
    update.setMode(ContentMode.STDERR);

    final Rule returned = metric.updateFrom(update);
    assertEquals(metric, returned);

    assertEquals(ContentMode.STDERR, metric.getMode());
  }

  @Test
  public void testUpdateMetricDetails ()
  {
    assertEquals(ContentMode.DEFAULT, metric.getMode());
    metric.updateFrom(new InstanceMetric());
    assertEquals(ContentMode.DEFAULT, metric.getMode());
  }
}
