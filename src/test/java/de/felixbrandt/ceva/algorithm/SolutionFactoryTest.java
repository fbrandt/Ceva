package de.felixbrandt.ceva.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.algorithm.SolutionFactory;
import de.felixbrandt.ceva.controller.MockCommand;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.InstanceSource;

public class SolutionFactoryTest
{
  Algorithm algorithm;
  AlgorithmExecutable executable;
  SolutionFactory factory;
  MockCommand result;

  @Before
  public void setUp () throws Exception
  {
    algorithm = new Algorithm();
    executable = new AlgorithmExecutable(algorithm);
    factory = new SolutionFactory(executable);
    result = new MockCommand();
  }

  @Test
  public void testCreate ()
  {
    final Object object = factory.create(result);
    assertEquals(Solution.class, object.getClass());
  }

  @Test
  public void testSetAlgorithm ()
  {
    final Solution solution = (Solution) factory.create(result);
    assertEquals(algorithm, solution.getAlgorithm());
  }

  @Test
  public void testSetInstance ()
  {
    final InstanceSource source = new InstanceSource(new Instance());
    factory.setSource(source);
    final Solution solution = (Solution) factory.create(result);
    assertEquals(source.getInstance(), solution.getInstance());
  }

  @Test
  public void testSetVersion ()
  {
    factory.setVersion(42);
    final Solution solution = (Solution) factory.create(result);
    assertEquals(42, solution.getVersion());
  }

  @Test
  public void testSetStdout ()
  {
    result.setStdout("stdout");
    final Solution solution = (Solution) factory.create(result);
    assertEquals("stdout", solution.getStdout());
  }

  @Test
  public void testSetStderr ()
  {
    result.setStderr("stderr");
    final Solution solution = (Solution) factory.create(result);
    assertEquals("stderr", solution.getStderr());
  }

  @Test
  public void testDefaultRuntime ()
  {
    result.setRuntime(42.42);
    final Solution solution = (Solution) factory.create(result);
    assertEquals(42.42, solution.getRuntime(), 0.01);
  }

  @Test
  public void testSetRuntime ()
  {
    result.setStderr("stderr");
    final Solution solution = (Solution) factory.create(result);
    assertEquals("stderr", solution.getStderr());
  }

  @Test
  public void testSetMachine ()
  {
    final Solution solution = (Solution) factory.create(result);
    assertFalse(solution.getMachine().equals(""));
  }

}
