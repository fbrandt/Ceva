package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.SolutionImportConfig;
import de.felixbrandt.ceva.SolutionImportService;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.provider.AlgorithmProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.storage.MockStorage;
import de.felixbrandt.support.StreamSupport;

public class SolutionImportServiceTest
{
  class MockInstanceProvider implements InstanceProvider
  {
    public String last_keyword;
    public Instance instance = new Instance();
    public int size = 1;

    public Collection<Instance> findByKeyword (final String keyword)
    {
      last_keyword = keyword;
      final Collection<Instance> result = new ArrayList<Instance>();

      for (int i = 0; i < size; ++i) {
        result.add(instance);
      }

      return result;
    }
  }

  class MockAlgorithmProvider implements AlgorithmProvider
  {
    public String last_name;
    public Algorithm algorithm = new Algorithm();

    public Algorithm findByName (final String name)
    {
      last_name = name;
      return algorithm;
    }
  }

  ByteArrayOutputStream output;
  ArrayList<String> args;
  MockInstanceProvider instance_provider;
  MockAlgorithmProvider algo_provider;
  MockStorage storage;
  SolutionImportService service;
  SolutionImportConfig config;

  @Before
  public void setUp () throws Exception
  {
    final InputStream stdin = StreamSupport.createInputStream("test");
    config = new SolutionImportConfig(stdin);

    instance_provider = new MockInstanceProvider();
    algo_provider = new MockAlgorithmProvider();
    storage = new MockStorage();
    output = new ByteArrayOutputStream();
    service = new SolutionImportService(new PrintStream(output), instance_provider,
            algo_provider, storage);
  }

  @Test
  public void testCreateDefaultSolution ()
  {
    final Instance instance = new Instance();
    final Algorithm algorithm = new Algorithm();

    final Solution solution = service.createSolution(instance, algorithm, 42, config);

    assertEquals(instance, solution.getInstance());
    assertEquals(algorithm, solution.getAlgorithm());
    assertEquals(42, solution.getVersion());
    assertEquals("imported", solution.getMachine());
    assertEquals("test", solution.getStdout());
  }

  @Test
  public void testCreateInitSolution ()
  {
    final Instance instance = new Instance();
    final Algorithm algorithm = new Algorithm();

    config.setMachine("MYMACHINE");
    config.setRuntime(1000);
    config.setParams("asdf");
    config.setStdoutFile("test/stdout_sample.txt");
    config.setStderrFile("test/stderr_sample.txt");

    final Solution solution = service.createSolution(instance, algorithm, 42, config);

    assertEquals(instance, solution.getInstance());
    assertEquals(algorithm, solution.getAlgorithm());
    assertEquals(42, solution.getVersion());
    assertEquals("MYMACHINE", solution.getMachine());
    assertEquals(1000.0, solution.getRuntime(), 0.001);
    assertEquals("asdf", solution.getParameters());
    assertEquals("output", solution.getStdout());
    assertEquals("errors", solution.getStderr());
  }

  @Test
  public void testResolveInstanceNull ()
  {
    Instance no_instance = service.resolveInstance(null);
    assertEquals(null, no_instance);
    assertTrue(output.toString(),
            output.toString().matches("(?s)(.*)no instance specified(.*)"));
  }

  @Test
  public void testResolveInstanceNotFound ()
  {
    instance_provider.size = 0;
    final Instance no_instance = service.resolveInstance("keyword");
    assertEquals(null, no_instance);
    assertTrue(output.toString(),
            output.toString().matches("(?s)(.*)instance keyword not found(.*)"));
  }

  @Test
  public void testResolveInstanceMatch ()
  {
    final Instance instance = service.resolveInstance("keyword");
    assertEquals("keyword", instance_provider.last_keyword);
    assertEquals(instance, instance_provider.instance);
  }

  @Test
  public void testResolveInstanceMultiple ()
  {
    instance_provider.size = 2;
    final Instance no_instance = service.resolveInstance("keyword");
    assertEquals(null, no_instance);
    assertTrue(output.toString(),
            output.toString().matches("(?s)(.*)instance keyword is ambiguous(.*)"));
  }

  @Test
  public void testResolveAlgorithmParamMissing ()
  {
    final Algorithm algo = service.resolveAlgorithm(null);
    assertEquals(null, algo);
    assertTrue(output.toString(),
            output.toString().matches("(?s)(.*)no algorithm specified(.*)"));
  }

  @Test
  public void testResolveAlgorithmNotFound ()
  {
    algo_provider.algorithm = null;
    final Algorithm algo = service.resolveAlgorithm("algoname");
    assertEquals(null, algo);
    assertTrue(output.toString(),
            output.toString().matches("(?s)(.*)algorithm algoname not found(.*)"));
  }

  @Test
  public void testResolveAlgorithm ()
  {
    final Algorithm algo = service.resolveAlgorithm("algoname");
    assertEquals("algoname", algo_provider.last_name);
    assertEquals(algo, algo_provider.algorithm);
  }

  @Test
  public void testRun ()
  {
    config.setInstanceKeyword("instance");
    config.setAlgorithmName("algorithm");
    config.setVersion(23);

    instance_provider.instance.setName("instancename");
    algo_provider.algorithm.setName("algoname");

    service.run(config);

    assertEquals(1, storage.getAddCount());
    final Solution solution = (Solution) storage.getLastAdded();
    assertEquals(instance_provider.instance, solution.getInstance());
    assertEquals("instance", instance_provider.last_keyword);
    assertEquals(algo_provider.algorithm, solution.getAlgorithm());
    assertEquals("algorithm", algo_provider.last_name);
    assertEquals(23, solution.getVersion());
    assertTrue(output.toString(), output.toString()
            .matches("(?s)(.*)imported solution to algoname for instance instancename(.*)"));
  }

  @Test
  public void testRunNoVersion ()
  {
    config.setInstanceKeyword("instance");
    config.setAlgorithmName("algorithm");

    service.run(config);

    assertEquals(1, storage.getAddCount());
    final Solution solution = (Solution) storage.getLastAdded();
    assertEquals(0, solution.getVersion());
  }

}
