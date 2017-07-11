package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.SolutionDBStorage;
import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.InstanceSource;

public class SolutionDBStorageTest
{
  SessionHandler session_handler;
  SolutionDBStorage storage;
  Algorithm algorithm;
  Instance instance;
  Solution solution;

  @Before
  public void setUp () throws Exception
  {
    session_handler = TestSessionBuilder.build();

    algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);
    instance = new Instance();
    session_handler.getSession().save(instance);

    solution = new Solution();
    solution.setAlgorithm(algorithm);
    solution.setInstance(instance);
    solution.setVersion(1);

    storage = new SolutionDBStorage(session_handler);
  }

  @After
  public void tearDown () throws Exception
  {
    session_handler.shutdown();
  }

  @Test
  public void testExistsExecutableDataSource ()
  {
    final AlgorithmExecutable executable = new AlgorithmExecutable(algorithm);
    final InstanceSource source = new InstanceSource(instance);

    assertFalse(storage.exists(executable, source, 1));
    session_handler.getSession().save(solution);
    assertTrue(storage.exists(executable, source, 1));
  }

  @Test
  public void testExistsExecutableParameters ()
  {
    final HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("paramA", "1");

    algorithm.setRunPath("cmd {paramA}");
    
    final AlgorithmExecutable executable_param = new AlgorithmExecutable(algorithm,
            parameters);
    final InstanceSource source = new InstanceSource(instance);
    solution.setParameters("paramA: 1;");

    assertFalse(storage.exists(executable_param, source, 1));
    session_handler.getSession().save(solution);
    assertTrue(storage.exists(executable_param, source, 1));
  }

  @Test
  public void testExistsAlgorithmInstance ()
  {
    assertFalse(storage.exists(algorithm, instance, "", 1));
    session_handler.getSession().save(solution);
    assertTrue(storage.exists(algorithm, instance, "", 1));
  }

  @Test
  public void testExistsAlgorithmInstanceVersion ()
  {
    assertFalse(storage.exists(algorithm, instance, "", 1));
    session_handler.getSession().save(solution);
    assertFalse(storage.exists(algorithm, instance, "", 2));
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
    assertEquals(0, session_handler.getSession().createQuery("from Solution").list().size());
    storage.add(solution);
    assertEquals(1, session_handler.getSession().createQuery("from Solution").list().size());

    solution = new Solution();
    solution.setAlgorithm(algorithm);
    solution.setInstance(instance);
    solution.setVersion(1);
    storage.add(solution);
    assertEquals(1, session_handler.getSession().createQuery("from Solution").list().size());
  }

  @Test
  public void testGetUnsolvedSolutionsFiltered ()
  {
    session_handler.getSession().save(solution);
    final Executable executable = new AlgorithmExecutable(algorithm);
    final HashSet<Integer> result = storage.getUnsolved(executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testSQLQuery ()
  {
    session_handler.getSession().flush();
    assertEquals(1, session_handler.getSession().createSQLQuery("SELECT i.instance FROM Instance as i").list().size());
    
    final String query = "SELECT i.instance FROM Instance AS i LEFT JOIN Solution AS s "
        + " ON (i.instance = s.instance) "
        + "GROUP BY i.instance HAVING COUNT(DISTINCT s.solution) < 2";
    assertEquals(1, session_handler.getSession().createSQLQuery(query).list().size());
  }

  @Test
  public void testGetUnsolvedSolutionsPartly ()
  {
    algorithm.setRepeat(2);
    session_handler.getSession().save(solution);
    final Executable executable = new AlgorithmExecutable(algorithm);
    final HashSet<Integer> result = storage.getUnsolved(executable, 1);
    assertEquals(1, result.size());
    assertTrue(result.contains(instance.getInstance()));
  }

  @Test
  public void testGetUnsolvedSolutions ()
  {
    final Executable executable = new AlgorithmExecutable(algorithm);
    final HashSet<Integer> result = storage.getUnsolved(executable, 1);
    assertEquals(1, result.size());
    assertTrue(result.contains(instance.getInstance()));
  }

  @Test
  public void testGetUnsolvedSolutionsVersionMismatch ()
  {
    session_handler.getSession().save(solution);
    final Executable executable = new AlgorithmExecutable(algorithm);
    final HashSet<Integer> result = storage.getUnsolved(executable, 2);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetUnsolvedSolutionsMismatch ()
  {
    session_handler.getSession().save(solution);
    final SolutionMetric other_metric = new SolutionMetric();
    session_handler.getSession().save(other_metric);

    final Executable executable = new AlgorithmExecutable(algorithm);
    final HashSet<Integer> result = storage.getUnsolved(executable, 1);
    assertEquals(0, result.size());
  }

  @Test
  public void testCountExecutableDataSource ()
  {
    final AlgorithmExecutable executable = new AlgorithmExecutable(algorithm);
    final InstanceSource source = new InstanceSource(instance);

    assertEquals(0, storage.count(executable, source, 1));
    session_handler.getSession().save(solution);

    assertEquals(1, storage.count(executable, source, 1));
  }

  @Test
  public void testCountExetuableParameterDataSource ()
  {
    final HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("paramA", "0");
    
    algorithm.setRunPath("cmd {paramA}");
    
    final AlgorithmExecutable executable = new AlgorithmExecutable(algorithm, parameters);
    final InstanceSource source = new InstanceSource(instance);

    solution.setParameters("paramA: 0;");

    assertEquals(0, storage.count(executable, source, 1));

    session_handler.getSession().save(solution);

    assertEquals(1, storage.count(executable, source, 1));

  }

  @Test
  public void testCountExecutableParameterDifferentSolutions ()
  {
    final HashMap<String, String> parametersA = new HashMap<String, String>();
    parametersA.put("paramA", "0");
    final HashMap<String, String> parametersB = new HashMap<String, String>();
    parametersB.put("paramA", "1");
    
    algorithm.setRunPath("cmd {paramA}");

    final AlgorithmExecutable executableA = new AlgorithmExecutable(algorithm, parametersA);
    final AlgorithmExecutable executableB = new AlgorithmExecutable(algorithm, parametersB);

    final InstanceSource source = new InstanceSource(instance);

    solution.setParameters("paramA: 0;");

    final Solution another_solution;
    another_solution = new Solution();
    another_solution.setAlgorithm(algorithm);
    another_solution.setInstance(instance);
    another_solution.setVersion(1);
    another_solution.setParameters("paramA: 1;");

    session_handler.getSession().save(solution);

    assertEquals(1, storage.count(executableA, source, 1));
    assertEquals(0, storage.count(executableB, source, 1));

    session_handler.getSession().save(another_solution);

    assertEquals(1, storage.count(executableA, source, 1));
    assertEquals(1, storage.count(executableB, source, 1));
  }

  @Test
  public void testCountAlgorithmInstance ()
  {
    assertEquals(0, storage.count(algorithm, instance, "", 1));
    session_handler.getSession().save(solution);
    assertEquals(1, storage.count(algorithm, instance, "", 1));
  }

  @Test
  public void testCountAlgorithmInstanceVersion ()
  {
    assertEquals(0, storage.count(algorithm, instance, "", 1));
    session_handler.getSession().save(solution);
    assertEquals(0, storage.count(algorithm, instance, "", 2));
  }
}
