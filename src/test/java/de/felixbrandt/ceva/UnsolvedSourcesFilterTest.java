package de.felixbrandt.ceva;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.InstanceDataDBStorage;
import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.UnsolvedSourcesFilter;
import de.felixbrandt.ceva.controller.MockVersionProvider;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.DataInteger;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.ceva.metric.MetricExecutable;

public class UnsolvedSourcesFilterTest
{
  SessionHandler session_handler;
  InstanceDataDBStorage storage;
  InstanceMetric metric;
  Instance instance;
  DataInteger<InstanceMetric, Instance> data;
  UnsolvedSourcesFilter filter;
  List<DataSource> sources;
  Iterable<? extends DataSource> filtered;

  @Before
  public void setUp ()
  {
    session_handler = TestSessionBuilder.build();

    metric = new InstanceMetric();
    session_handler.getSession().save(metric);
    instance = new Instance();
    session_handler.getSession().save(instance);

    data = new InstanceDataInteger();
    data.setRule(metric);
    data.setSource(instance);
    data.setVersion(1);

    storage = new InstanceDataDBStorage(session_handler);
    sources = new ArrayList<DataSource>();
    filter = new UnsolvedSourcesFilter(storage, new MockVersionProvider(1));
  }

  @After
  public void tearDown ()
  {
    session_handler.shutdown();
  }

  @Test
  public void testdoFilterUnknownInstance ()
  {
    // non-stored instances are filtered out
    sources.add(new InstanceSource(new Instance()));
    filtered = filter.doFilter(new InstanceMetricExecutable(metric), sources);
    assertFalse(filtered.iterator().hasNext());
  }

  @Test
  public void testdoFilterNoVersion ()
  {
    final Instance other_instance = new Instance();
    session_handler.getSession().save(other_instance);

    sources.add(new InstanceSource(other_instance));
    filtered = filter.doFilter(new InstanceMetricExecutable(metric), sources);
    assertTrue(filtered.iterator().hasNext());
  }

  @Test
  public void testdoFilterPresent ()
  {
    session_handler.getSession().save(data);
    sources.add(new InstanceSource(instance));
    filtered = filter.doFilter(new InstanceMetricExecutable(metric), sources);
    assertFalse(filtered.iterator().hasNext());
  }

  @Test
  public void testdoFilterOldVersionDirect ()
  {
    final ArrayList<DataSource> list = new ArrayList<DataSource>();
    list.add(new InstanceSource(instance));

    final Executable executable = new InstanceMetricExecutable(metric);
    filtered = filter.doFilter(executable, 2, list);
    assertTrue(filtered.iterator().hasNext());
  }

}
