package de.felixbrandt.ceva.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.InstanceMetric;

public class InstanceMetricServiceTest
{
  SessionHandler session_handler;
  InstanceMetric metric;
  InstanceMetricService service;

  @Before
  public void setup ()
  {
    session_handler = TestSessionBuilder.build();
    metric = new InstanceMetric();
    metric.setName("test");
    metric.setActive(true);
    session_handler.getSession().save(metric);
    service = new InstanceMetricService(session_handler);
  }

  @After
  public void tearDown () throws Exception
  {
    session_handler.rollback();
  }

  @Test
  public void testGetAll ()
  {
    assertEquals(1, service.getAll().size());
  }

  @Test
  public void testGetActive ()
  {
    assertEquals(1, service.getActive().size());
  }

  @Test
  public void testGetActiveSkipInactive ()
  {
    metric.setActive(false);
    session_handler.getSession().save(metric);

    assertEquals(0, service.getActive().size());
  }

  @Test
  public void testGetByName ()
  {
    assertEquals(metric, service.getByName("test"));
  }

  @Test
  public void testGetByNameFail ()
  {
    assertNull(service.getByName("other"));
  }

  @Test
  public void testUpdateAll ()
  {
    final InstanceMetric metric_update = new InstanceMetric();
    metric_update.setName("newrule");
    final ArrayList list = new ArrayList();
    list.add(metric_update);
    service.update(list);

    assertEquals(2, service.getAll().size());
  }

  @Test
  public void testUpdateChange ()
  {
    final InstanceMetric metric_update = new InstanceMetric();
    metric_update.setName("test");
    metric_update.setBasePath("changed_path");
    service.update(metric_update);

    assertEquals("changed_path", metric.getBasePath());
  }

  @Test
  public void testUpdateCreate ()
  {
    final InstanceMetric metric_update = new InstanceMetric();
    metric_update.setName("newrule");
    service.update(metric_update);

    assertEquals(2, service.getAll().size());
  }
}
