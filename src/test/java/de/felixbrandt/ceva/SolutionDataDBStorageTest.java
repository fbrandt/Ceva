package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.DataDBStorage;
import de.felixbrandt.ceva.SolutionDataDBStorage;
import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.DataInteger;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.entity.SolutionDataInteger;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.SolutionMetricExecutable;
import de.felixbrandt.ceva.metric.SolutionSource;

public class SolutionDataDBStorageTest
{
  SessionHandler session_handler;
  DataDBStorage storage;
  SolutionMetric metric;
  Instance instance;
  Solution solution;
  DataInteger<SolutionMetric, Solution> data;

  @Before
  public void setUp ()
  {
    session_handler = TestSessionBuilder.build();

    instance = new Instance();
    instance.setInstance(1);
    session_handler.getSession().save(instance);
    final Algorithm algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);
    solution = new Solution();
    solution.setAlgorithm(algorithm);
    solution.setInstance(instance);
    solution.setSolution(1);
    session_handler.getSession().save(solution);
    metric = new SolutionMetric();
    metric.setType(MetricType.INT_METRIC);
    session_handler.getSession().save(metric);

    data = new SolutionDataInteger();
    data.setRule(metric);
    data.setSource(solution);
    data.setVersion(1);

    storage = new SolutionDataDBStorage(session_handler);
  }

  @After
  public void tearDown ()
  {
    session_handler.shutdown();
  }

  @Test
  public void testExistsExecutableDataSource ()
  {
    final Executable executable = new SolutionMetricExecutable(metric);
    final SolutionSource source = new SolutionSource(solution);

    assertFalse(storage.exists(executable, source, 1));
    session_handler.getSession().save(data);
    assertTrue(storage.exists(executable, source, 1));
  }

  @Test
  public void testExistsSerialized () throws IOException, ClassNotFoundException
  {
    final ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
    final ObjectOutputStream data_serializer = new ObjectOutputStream(out_stream);
    data_serializer.writeObject(data);

    final ByteArrayInputStream in_stream = new ByteArrayInputStream(out_stream.toByteArray());
    final ObjectInputStream data_deserializer = new ObjectInputStream(in_stream);
    final SolutionDataInteger data_copy = (SolutionDataInteger) data_deserializer.readObject();

    assertFalse(storage.exists(data_copy.getRule(), data_copy.getSource(), 1));
    session_handler.getSession().save(data);
    assertTrue(storage.exists(data_copy.getRule(), data_copy.getSource(), 1));
  }

  @Test
  public void testGetUnsolvedSolutionsFiltered ()
  {
    session_handler.getSession().save(data);
    final Executable solution_metric_executable = new SolutionMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(solution_metric_executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetUnsolvedSolutions ()
  {

    final Executable solution_metric_executable = new SolutionMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(solution_metric_executable, 1);
    assertEquals(1, result.size());
    assertTrue(result.contains(instance.getInstance()));
  }

  @Test
  public void testGetUnsolvedSolutionsVersionMismatch ()
  {
    session_handler.getSession().save(data);
    final Executable solution_metric_executable = new SolutionMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(solution_metric_executable, 2);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetUnsolvedSolutionsMismatch ()
  {
    session_handler.getSession().save(data);
    final SolutionMetric other_metric = new SolutionMetric();
    session_handler.getSession().save(other_metric);

    final Executable solution_metric_executable = new SolutionMetricExecutable(metric);
    final HashSet<Integer> result = storage.getUnsolved(solution_metric_executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testCountExecutableDataSource ()
  {
    final Executable executable = new SolutionMetricExecutable(metric);
    final SolutionSource source = new SolutionSource(solution);

    assertFalse(storage.exists(executable, source, 1));
    assertEquals(0, storage.count(executable, source, 1));

    session_handler.getSession().save(data);

    assertTrue(storage.exists(executable, source, 1));
    assertEquals(1, storage.count(executable, source, 1));

  }

  @Test
  public void testCountAlgorithmInstance ()
  {
    assertEquals(0, storage.count(metric, instance, 1));
    session_handler.getSession().save(data);
    assertEquals(1, storage.count(metric, instance, 1));
  }

  @Test
  public void testCountAlgorithmInstanceVersion ()
  {
    assertEquals(0, storage.count(metric, instance, 1));
    session_handler.getSession().save(data);
    assertEquals(0, storage.count(metric, instance, 2));
  }

  @Test
  public void testGetTablename ()
  {
    assertEquals("SolutionDataInteger", storage.getTablename(metric));

    metric.setType(MetricType.STRING_METRIC);
    assertEquals("SolutionDataString", storage.getTablename(metric));

    metric.setType(MetricType.DOUBLE_METRIC);
    assertEquals("SolutionDataDouble", storage.getTablename(metric));

    SolutionMetric blanc_metric = new SolutionMetric();
    assertEquals("SolutionDataInteger", storage.getTablename(blanc_metric));
  }

}
