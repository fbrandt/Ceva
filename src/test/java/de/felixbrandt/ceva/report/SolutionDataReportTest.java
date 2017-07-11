package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.MetricType;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.entity.SolutionDataInteger;
import de.felixbrandt.ceva.entity.SolutionDataString;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.provider.FixedAlgorithmProvider;
import de.felixbrandt.ceva.provider.FixedInstanceProvider;
import de.felixbrandt.ceva.provider.FixedSolutionProvider;
import de.felixbrandt.ceva.report.SolutionDataReport;

public class SolutionDataReportTest
{
  ByteArrayOutputStream stream;
  SolutionDataReport report;
  FixedInstanceProvider instance_provider;
  FixedAlgorithmProvider algorithm_provider;
  FixedSolutionProvider solution_provider;

  @Before
  public void setUp () throws Exception
  {
    stream = new ByteArrayOutputStream();
    instance_provider = new FixedInstanceProvider();
    algorithm_provider = new FixedAlgorithmProvider(new Algorithm());
    solution_provider = new FixedSolutionProvider();

    report = new SolutionDataReport(new PrintStream(stream), instance_provider,
            algorithm_provider, solution_provider, null, null);
  }

  @Test
  public void testPrintHeader ()
  {
    report.printHeader(new PrintStream(stream));
    final String result = stream.toString();
    assertTrue(result, result.matches(
            "(?s)(.*)ID;INSTANCE;ALGORITHM;PARAMETERS;VERSION;METRIC;VALUE;REPEAT(.*)"));
  }

  @Test
  public void testPrintRow ()
  {
    final Instance instance = new Instance("INSTANCE");
    final Algorithm algorithm = new Algorithm("ALGO");
    final Solution solution = new Solution(instance, algorithm, 23);
    final SolutionMetric metric = new SolutionMetric("METRIC");
    final SolutionDataInteger data = new SolutionDataInteger(solution, metric, 0, 42);

    report.printRow(new PrintStream(stream), data);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*);INSTANCE;ALGO;;23;METRIC;42;(.*)"));
  }

  @Test
  public void testPrintStringRow ()
  {
    final Instance instance = new Instance("INSTANCE");
    final Algorithm algorithm = new Algorithm("ALGO");
    final Solution solution = new Solution(instance, algorithm, 23);
    final SolutionMetric metric = new SolutionMetric("METRIC");
    metric.setType(MetricType.STRING_METRIC);
    final SolutionDataString data = new SolutionDataString(solution, metric, 0, "foobar");

    report.printRow(new PrintStream(stream), data);
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*);INSTANCE;ALGO;;23;METRIC;foobar;(.*)"));
  }

  @Test
  public void testRetrieveSourcesArgumentsMissing ()
  {
    final ArrayList<String> args = new ArrayList<String>();
    args.add("x");
    args.add("INSTANCE");
    args.add("SOMEMETRIC");
    args.add("ALGORITHM");

    report.retrieveSources(args, 1);

    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)not enough arguments(.*)"));
  }

  @Test
  public void testRetrieveNoAlgorithm ()
  {
    algorithm_provider.setAlgorithm(null);
    final ArrayList<String> args = new ArrayList<String>();
    args.add("I");
    args.add("M");
    args.add("A");
    args.add("23");

    assertNull(report.retrieveSources(args, 0));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)algorithm (.*) not found(.*)"));
  }

  @Test
  public void testRetrieveVersionFail ()
  {
    final ArrayList<String> args = new ArrayList<String>();
    args.add("I");
    args.add("M");
    args.add("A");
    args.add("XXX");

    assertNull(report.retrieveSources(args, 0));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)could not parse version(.*)"));
  }

  @Test
  public void testRetrieveSources ()
  {
    instance_provider.add(new Instance());
    final ArrayList<String> args = new ArrayList<String>();
    args.add("x");
    args.add("INSTANCE");
    args.add("SOMEMETRIC");
    args.add("ALGORITHM");
    args.add("23");

    report.retrieveSources(args, 1);

    assertEquals("INSTANCE", instance_provider.getLastKeyword());
    assertEquals("ALGORITHM", algorithm_provider.getLastName());
    assertEquals(23, solution_provider.getLastVersion());
  }

  @Test
  public void testRetrieveSourcesWithParameterInstantiation ()
  {
    instance_provider.add(new Instance());
    String[] parameters = { "-i", "INSTANCE", "-m", "SOMEMETRIC", "-a", "ALGORITHM", "-v",
        "23" };
    new JCommander(report, parameters);

    report.retrieveSources();

    assertEquals("INSTANCE", instance_provider.getLastKeyword());
    assertEquals("ALGORITHM", algorithm_provider.getLastName());
    assertEquals(23, solution_provider.getLastVersion());
  }

  @Test
  public void testRetrieveSourcesWithParameterInstantiationNoAlgorithmFound ()
  {
    algorithm_provider = new FixedAlgorithmProvider(null);
    report = new SolutionDataReport(new PrintStream(stream), instance_provider,
            algorithm_provider, solution_provider, null, null);

    assertEquals(null, report.retrieveSources());
  }

  @Test
  public void testRetrieveSolutions ()
  {
    solution_provider.add(new Solution());
    solution_provider.add(new Solution());

    final ArrayList<Instance> instances = new ArrayList<Instance>();
    instances.add(new Instance("first"));
    instances.add(new Instance("last"));
    final Algorithm algorithm = new Algorithm("algo");

    final Collection<Solution> solutions = report.retrieveSolutions(instances, algorithm, 42);
    assertEquals(4, solutions.size());
    assertEquals(instances.get(1), solution_provider.getLastInstance());
    assertEquals(algorithm, solution_provider.getLastAlgorithm());
    assertEquals(42, solution_provider.getLastVersion());
  }

  @Test
  public void testParameterInstantiationWithJCommanderAll ()
  {
    String[] parameters = { "-i", "instance", "-a", "algorithm", "-v", "0" };
    new JCommander(report, parameters);
  }

  @Test
  public void testRetrieveSourcesWithIllegalNumberFormat ()
  {
    instance_provider.add(new Instance());
    String[] parameters = { "-i", "INSTANCE", "-m", "SOMEMETRIC", "-a", "ALGORITHM", "-v",
        "WRONG_FORMAT" };
    new JCommander(report, parameters);

    report.retrieveSources();

    assertEquals("INSTANCE", instance_provider.getLastKeyword());
    assertEquals("ALGORITHM", algorithm_provider.getLastName());
  }

  @Test(expected = com.beust.jcommander.ParameterException.class)
  public void testParameterInstatiationWithJCommanderWithoutRequired ()
  {
    String[] parameters = { "-i", "instance", "-v", "version" };
    new JCommander(report, parameters);
  }
}
