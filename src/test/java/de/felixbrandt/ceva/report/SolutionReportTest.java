package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.provider.FixedAlgorithmProvider;
import de.felixbrandt.ceva.provider.FixedInstanceProvider;
import de.felixbrandt.ceva.provider.FixedSolutionProvider;
import de.felixbrandt.ceva.report.SolutionReport;

public class SolutionReportTest
{
  ByteArrayOutputStream stream;
  Instance instance;
  FixedInstanceProvider instance_provider;
  Algorithm algorithm;
  FixedAlgorithmProvider algorithm_provider;
  FixedSolutionProvider solution_provider;
  SolutionReport report;
  List<String> args;

  @Before
  public void setUp () throws Exception
  {
    stream = new ByteArrayOutputStream();
    args = new ArrayList<String>();
    instance = new Instance();
    instance_provider = new FixedInstanceProvider();
    instance_provider.add(instance);

    algorithm = new Algorithm();
    algorithm_provider = new FixedAlgorithmProvider(algorithm);

    solution_provider = new FixedSolutionProvider();

    report = new SolutionReport(new PrintStream(stream), instance_provider, algorithm_provider,
            solution_provider);
  }

  @Test
  public void testRunFail ()
  {
    report.run(args, 0);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)parameter format unknown(.*)"));
  }

  @Test
  public void testRunID ()
  {
    final Solution mysolution = new Solution();
    mysolution.setStdout("CONTENT");
    solution_provider.add(mysolution);
    args.add("123");
    report.run(args, 0);
    assertEquals(123, solution_provider.getLastId());
    assertTrue(stream.toString(), stream.toString().matches("(?s)(.*)CONTENT(.*)"));
  }

  @Test
  public void testRunParams ()
  {
    final Solution mysolution = new Solution();
    mysolution.setStdout("CONTENT");
    solution_provider.add(mysolution);
    args.add("inst");
    args.add("algo");
    report.run(args, 0);
    assertEquals(instance, solution_provider.getLastInstance());
    assertEquals(algorithm, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunParamsVersion ()
  {
    final Solution mysolution = new Solution();
    mysolution.setStdout("CONTENT");
    solution_provider.add(mysolution);
    args.add("inst");
    args.add("algo");
    args.add("42");
    report.run(args, 0);
    assertEquals(42, solution_provider.getLastVersion());
  }

  @Test
  public void testRunByIDNaN ()
  {
    final Solution solution = report.run("foobar");
    assertNull(solution);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)could not parse solution ID(.*)"));
  }

  @Test
  public void testRunByIDFail ()
  {
    final Solution solution = report.run("123");
    assertNull(solution);
    assertEquals(123, solution_provider.getLastId());
  }

  @Test
  public void testRunByIDMatch ()
  {
    final Solution mysolution = new Solution();
    solution_provider.add(mysolution);
    final Solution result = report.run("3");
    assertEquals(mysolution, result);
  }

  @Test
  public void testRunByParams ()
  {
    final Solution mysolution = new Solution();
    solution_provider.add(mysolution);
    final Solution result = report.run("inst", "algo");
    assertEquals(mysolution, result);
    assertEquals(instance, solution_provider.getLastInstance());
    assertEquals(algorithm, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunByParamsNoInstance ()
  {
    instance_provider.reset();
    assertNull(report.run("inst", "algo"));
    assertEquals(null, solution_provider.getLastInstance());
    assertEquals(null, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunByParamsNoAlgorithm ()
  {
    algorithm_provider.setAlgorithm(null);
    assertNull(report.run("inst", "algo"));
    assertEquals(null, solution_provider.getLastInstance());
    assertEquals(null, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunByParamsAmbiguous ()
  {
    final Solution mysolution = new Solution();
    solution_provider.add(mysolution);
    solution_provider.add(mysolution);
    final Solution result = report.run("inst", "algo");
    assertNull(result);
  }

  @Test
  public void testRunByParamsVersionNaN ()
  {
    final Solution result = report.run("inst", "algo", "nan");
    assertEquals(null, result);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)could not parse solution version(.*)"));
  }

  @Test
  public void testRunByParamsVersion ()
  {
    final Solution mysolution = new Solution();
    solution_provider.add(mysolution);
    final Solution result = report.run("inst", "algo", "42");
    assertEquals(mysolution, result);
    assertEquals(instance, solution_provider.getLastInstance());
    assertEquals(algorithm, solution_provider.getLastAlgorithm());
    assertEquals(42, solution_provider.getLastVersion());
  }

  @Test
  public void testRunByParamsVersionNoInstance ()
  {
    instance_provider.reset();
    assertNull(report.run("inst", "algo", "42"));
    assertEquals("inst", instance_provider.getLastKeyword());
    assertEquals(null, solution_provider.getLastInstance());
    assertEquals(null, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunByParamsVersionNoAlgorithm ()
  {
    algorithm_provider.setAlgorithm(null);
    assertNull(report.run("inst", "algo", "42"));
    assertEquals("algo", algorithm_provider.getLastName());
    assertEquals(null, solution_provider.getLastInstance());
    assertEquals(null, solution_provider.getLastAlgorithm());
  }

  @Test
  public void testRunWithParameterInstantiationById ()
  {
    final Solution solution = new Solution();
    solution_provider.add(solution);

    String[] parameter = { "-s", "1" };
    new JCommander(report, parameter);

    report.run();

    assertEquals(1, solution_provider.getLastId());
  }

  @Test
  public void testRunWithParameterInstatiationByInstanceAlgorithmVersion ()
  {
    final Solution solution = new Solution();
    solution_provider.add(solution);

    String[] parameter = { "-i", "INSTANCE", "-a", "ALGORITHM", "-v", "1" };
    new JCommander(report, parameter);
    report.run();

    assertEquals("INSTANCE", instance_provider.getLastKeyword());
    assertEquals("ALGORITHM", algorithm_provider.getLastName());
    assertEquals(1, solution_provider.getLastVersion());
  }

  @Test
  public void testRunByParamsVersionAmbiguous ()
  {
    final Solution mysolution = new Solution();
    solution_provider.add(mysolution);
    solution_provider.add(mysolution);
    final Solution result = report.run("inst", "algo", "42");
    assertNull(result);
  }

  @Test
  public void testProcessSolutionsNone ()
  {
    final ArrayList<Solution> solutions = new ArrayList<Solution>();
    final Solution result = report.processSolutions(solutions);
    assertEquals(null, result);
    assertTrue(stream.toString(), stream.toString().matches("(?s)(.*)no solutions found(.*)"));
  }

  @Test
  public void testProcessSolutionsSingle ()
  {
    final Solution solution = new Solution();
    final ArrayList<Solution> solutions = new ArrayList<Solution>();
    solutions.add(solution);
    final Solution result = report.processSolutions(solutions);
    assertEquals(solution, result);
  }

  @Test
  public void testProcessSolutionsMultiple ()
  {
    final Solution solution = new Solution(instance, algorithm, 42);
    solution.setSolution(123);
    solution.setMachine("machine");
    solution.setRuntime(5555);
    final ArrayList<Solution> solutions = new ArrayList<Solution>();
    solutions.add(solution);
    solutions.add(solution);

    final Solution result = report.processSolutions(solutions);
    assertEquals(null, result);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)multiple solutions found:(.*)"));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)123(.*)42(.*)machine(.*)5555(.*)"));
  }

  @Test
  public void testResolveInstance ()
  {
    final Instance result = report.resolveInstance("test");
    assertEquals("test", instance_provider.getLastKeyword());
    assertEquals(instance, result);
  }

  @Test
  public void testResolveInstanceNone ()
  {
    instance_provider.reset();
    final Instance result = report.resolveInstance("test");
    assertEquals(null, result);
    assertTrue(stream.toString(), stream.toString().matches("(?s)(.*)instance not found(.*)"));
  }

  @Test
  public void testResolveInstanceAmbiguous ()
  {
    instance_provider.add(new Instance());
    final Instance result = report.resolveInstance("test");
    assertEquals(null, result);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)instance is ambiguous(.*)"));
  }

  @Test
  public void testResolveAlgorithm ()
  {
    final Algorithm result = report.resolveAlgorithm("test");
    assertEquals("test", algorithm_provider.getLastName());
    assertEquals(algorithm, result);
  }

  @Test
  public void testResolveAlgorithmFail ()
  {
    algorithm_provider.setAlgorithm(null);
    final Algorithm result = report.resolveAlgorithm("test");
    assertEquals(null, result);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)algorithm not found(.*)"));
  }

  @Test
  public void testParameterInstantiationWithJCommander ()
  {
    String[] parameters = { "-s", "id", "-i", "instance", "-a", "algorithm", "-v", "version" };

    new JCommander(report, parameters);

    assertEquals("id", report.getSolutionId());
    assertEquals("instance", report.getInstanceKeyword());
    assertEquals("algorithm", report.getAlgorithmName());
    assertEquals("version", report.getSolutionVersion());
  }
}
