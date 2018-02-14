package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.DataInteger;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceDataInteger;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;
import de.felixbrandt.ceva.metric.InstanceSource;

public class InstanceDataDBStorageTest
{
  SessionHandler session_handler;
  InstanceDataDBStorage storage;
  InstanceMetric metric;
  Instance instance;
  DataInteger<InstanceMetric, Instance> data;

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
  }

  @After
  public void tearDown ()
  {
    session_handler.shutdown();
  }

  @Test
  public void testExistsExecutableDataSource ()
  {
    final InstanceMetricExecutable executable = new InstanceMetricExecutable(metric);
    final InstanceSource source = new InstanceSource(instance);

    assertFalse(storage.exists(executable, source, 1));
    session_handler.getSession().save(data);
    assertTrue(storage.exists(executable, source, 1));
  }

  @Test
  public void testExistsMetricInstance ()
  {
    assertFalse(storage.exists(metric, instance, 1));

    session_handler.getSession().save(data);
    assertTrue(storage.exists(metric, instance, 1));
  }

  @Test
  public void testExistsMetricInstanceVersion ()
  {
    session_handler.getSession().save(data);
    assertTrue(storage.exists(metric, instance, 1));
    assertFalse(storage.exists(metric, instance, 2));
  }

  @Test
  public void testAddFail ()
  {
    storage.add(storage);
    assertEquals(0, session_handler.getSession().createQuery("from Data").list().size());
  }

  @Test
  public void testAdd ()
  {
    assertEquals(0, session_handler.getSession().createQuery("from Data").list().size());
    storage.add(data);
    assertEquals(1, session_handler.getSession().createQuery("from Data").list().size());
    storage.add(data);
    assertEquals(1, session_handler.getSession().createQuery("from Data").list().size());
  }

  @Test
  public void testExistsSerialized () throws IOException, ClassNotFoundException
  {
    final ByteArrayOutputStream metric_stream = new ByteArrayOutputStream();
    final ObjectOutputStream metric_serializer = new ObjectOutputStream(metric_stream);
    metric_serializer.writeObject(metric);

    final ByteArrayOutputStream instance_stream = new ByteArrayOutputStream();
    final ObjectOutputStream instance_serializer = new ObjectOutputStream(instance_stream);
    instance_serializer.writeObject(instance);

    final ByteArrayInputStream metric_copy_stream = new ByteArrayInputStream(
            metric_stream.toByteArray());
    final ObjectInputStream metric_deserializer = new ObjectInputStream(metric_copy_stream);
    final Metric metric_copy = (Metric) metric_deserializer.readObject();

    final ByteArrayInputStream instance_copy_stream = new ByteArrayInputStream(
            instance_stream.toByteArray());
    final ObjectInputStream instance_deserializer = new ObjectInputStream(
            instance_copy_stream);
    final Object instance_copy = instance_deserializer.readObject();

    assertFalse(storage.exists(metric_copy, instance_copy, 1));
    session_handler.getSession().save(data);
    assertTrue(storage.exists(metric_copy, instance_copy, 1));
  }

  @Test
  public void testGetUnsolvedInstancesFiltered ()
  {
    session_handler.getSession().save(data);
    final Executable metric_executable = new InstanceMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(metric_executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetUnsolvedInstances ()
  {

    final Executable metric_executable = new InstanceMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(metric_executable, 1);
    assertEquals(1, result.size());
    assertTrue(result.contains(instance.getInstance()));
  }

  @Test
  public void testGetUnsolvedInstancesVersionMismatch ()
  {
    session_handler.getSession().save(data);

    final Executable metric_executable = new InstanceMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(metric_executable, 2);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetUnsolvedInstancesMismatch ()
  {
    session_handler.getSession().save(data);
    final InstanceMetric other_metric = new InstanceMetric();
    session_handler.getSession().save(other_metric);

    final Executable metric_executable = new InstanceMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(metric_executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testCountExecutableDataSource ()
  {
    final InstanceMetricExecutable executable = new InstanceMetricExecutable(metric);
    final InstanceSource source = new InstanceSource(instance);

    assertEquals(0, storage.count(executable, source, 1));
    session_handler.getSession().save(data);
    assertEquals(1, storage.count(executable, source, 1));
  }

  @Test
  public void testCountMetricInstance ()
  {
    assertEquals(0, storage.count(metric, instance, 1));

    session_handler.getSession().save(data);
    assertEquals(1, storage.count(metric, instance, 1));
  }

  @Test
  public void testCountMetricInstanceVersion ()
  {
    session_handler.getSession().save(data);
    assertEquals(1, storage.count(metric, instance, 1));
    assertEquals(0, storage.count(metric, instance, 2));
  }

  @Test
  public void testGetDataMatchRule ()
  {
    session_handler.getSession().save(data);

    final InstanceMetric other_metric = new InstanceMetric();
    session_handler.getSession().save(other_metric);

    final InstanceDataInteger other_data = new InstanceDataInteger();
    other_data.setRule(other_metric);
    other_data.setSource(instance);
    session_handler.getSession().save(other_data);

    final Collection<Data<?, ?>> result = storage.getData(metric, instance);
    assertEquals(1, result.size());
    assertEquals(data, result.iterator().next());
  }

  @Test
  public void testGetDataMatchInstance ()
  {
    session_handler.getSession().save(data);

    final Instance other_instance = new Instance();
    session_handler.getSession().save(other_instance);

    final InstanceDataInteger other_data = new InstanceDataInteger();
    other_data.setRule(metric);
    other_data.setSource(other_instance);
    session_handler.getSession().save(other_data);

    final Collection<Data<?, ?>> result = storage.getData(metric, instance);
    assertEquals(1, result.size());
    assertEquals(data, result.iterator().next());
  }

  @Test
  public void testGetDataLastVersion ()
  {
    session_handler.getSession().save(data);

    final Instance other_instance = new Instance();
    session_handler.getSession().save(other_instance);

    final InstanceDataInteger other_data = new InstanceDataInteger();
    other_data.setRule(metric);
    other_data.setSource(instance);
    other_data.setVersion(0);
    session_handler.getSession().save(other_data);

    final Collection<Data<?, ?>> result = storage.getData(metric, instance);
    assertEquals(1, result.size());
    assertEquals(data, result.iterator().next());
  }

  @Test
  public void testGetTablename ()
  {
    assertEquals("InstanceDataInteger", storage.getTablename(metric));

    metric.setType(MetricType.STRING_METRIC);
    assertEquals("InstanceDataString", storage.getTablename(metric));

    metric.setType(MetricType.DOUBLE_METRIC);
    assertEquals("InstanceDataDouble", storage.getTablename(metric));

    SolutionMetric blanc_metric = new SolutionMetric();
    assertEquals("SolutionDataInteger", storage.getTablename(blanc_metric));
  }
}
