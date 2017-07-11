package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;
import de.felixbrandt.ceva.provider.InstanceMetricDBProvider;

public class InstanceMetricDBProviderTest
{
  SessionHandler session_handler;
  InstanceMetricDBProvider provider;

  @Before
  public void setUp () throws Exception
  {
    session_handler = TestSessionBuilder.build();
    provider = new InstanceMetricDBProvider(session_handler);
  }

  @After
  public void tearDown () throws Exception
  {
    session_handler.rollback();
  }

  @Test
  public void testGetNone ()
  {
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testGetOne ()
  {
    final InstanceMetric metric = new InstanceMetric();
    session_handler.getSession().save(metric);

    final Collection<? extends Executable> coll = provider.getExecutables();
    assertEquals(1, coll.size());
    assertEquals(InstanceMetricExecutable.class, coll.iterator().next().getClass());
  }

  @Test
  public void testGetNoInactive ()
  {
    final InstanceMetric metric = new InstanceMetric();
    metric.setActive(false);
    session_handler.getSession().save(metric);
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testGetNoSolutionMetric ()
  {
    final SolutionMetric metric = new SolutionMetric();
    session_handler.getSession().save(metric);
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testFindByName ()
  {
    final InstanceMetric metric = new InstanceMetric();
    metric.setName("foobar");
    session_handler.getSession().save(metric);

    Collection<? extends Metric> coll = null;
    coll = provider.findByName("foobar");
    assertEquals(1, coll.size());
    assertEquals(metric, coll.iterator().next());

    coll = provider.findByName("other");
    assertEquals(0, coll.size());

    coll = provider.findByName("");
    assertEquals(1, coll.size());
    assertEquals(metric, coll.iterator().next());

    coll = provider.findByName("all");
    assertEquals(1, coll.size());
    assertEquals(metric, coll.iterator().next());
  }
}
