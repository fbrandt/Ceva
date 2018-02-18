package de.felixbrandt.ceva.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Algorithm;

public class AlgorithmExecutableTest
{
  Algorithm algorithm;
  AlgorithmExecutable executable;

  @Before
  public void setUp () throws Exception
  {
    algorithm = new Algorithm();
    executable = new AlgorithmExecutable(algorithm);
  }

  @Test
  public void testGetFullVersionPath ()
  {
    algorithm.setVersionPath("VERSIONPATH");
    assertEquals("VERSIONPATH", executable.getFullVersionPath());
  }

  @Test
  public void testGetFullRunPath ()
  {
    algorithm.setRunPath("RUNPATH");
    assertEquals("RUNPATH", executable.getFullRunPath());
  }

  @Test
  public void testGetFullRunPathParameter ()
  {
    algorithm.setRunPath("cmd1 {param1} | cmd2 {param2}");

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");
    params.put("param2", "val2");

    executable.setParameters(params);

    assertEquals("cmd1 val1 | cmd2 val2", executable.getFullRunPath());
  }

  @Test
  public void testToString ()
  {
    algorithm.setName("test");
    assertEquals("algorithm test", executable.toString());
  }

  @Test
  public void testGetName ()
  {
    algorithm.setName("test");
    assertEquals("test", executable.getName());
  }

  @Test
  public void testGetResultFactory ()
  {
    final ResultFactory factory = executable.getResultFactory();

    assertEquals(SolutionFactory.class, factory.getClass());
    final SolutionFactory my_factory = (SolutionFactory) factory;

    assertEquals(executable, my_factory.getAlgorithmExecutable());
    assertEquals(algorithm, my_factory.getAlgorithmExecutable().getAlgorithm());
  }

  @Test
  public void testDefaultParameters ()
  {
    assertEquals(0, executable.getParameters().size());
  }

  @Test
  public void testParametersAsStringDefault ()
  {
    assertEquals("", executable.getParametersAsString());
  }

  @Test
  public void testParametersAsString ()
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("a", "1");
    parameters.put("b", "2");

    algorithm.setRunPath("{a} {b}");

    AlgorithmExecutable param_executable = new AlgorithmExecutable(algorithm,
            parameters);

    String params = param_executable.getParametersAsString();
    assertEquals("a: 1;b: 2;", params);
  }

  @Test
  public void testParameterAsStringOrder ()
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("a", "1");
    parameters.put("x", "a");
    parameters.put("z", "b");
    parameters.put("11a", "1");
    parameters.put("11b", "0");
    parameters.put("b", "2");

    algorithm.setRunPath("{a} {x} {z} {11a} {11b} {b}");
    AlgorithmExecutable param_executable = new AlgorithmExecutable(algorithm,
            parameters);

    String params = param_executable.getParametersAsString();
    assertEquals("11a: 1;11b: 0;a: 1;b: 2;x: a;z: b;", params);
  }

  @Test
  public void testParameterInConstructor ()
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("a", "1");
    parameters.put("b", "2");
    algorithm.setRunPath("{a} {b}");

    AlgorithmExecutable param_executable = new AlgorithmExecutable(algorithm,
            parameters);

    assertEquals(2, param_executable.getParameters().size());
    assertEquals("1", param_executable.getParameters().get("a"));
    assertEquals("2", param_executable.getParameters().get("b"));
  }

  @Test
  public void testGenerateParametersCorrectAmount ()
  {
    HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
    parameters.put("paramA", Arrays.asList("a1", "a2"));
    parameters.put("paramB", Arrays.asList("b1"));
    algorithm.setParameters(parameters);

    List<Executable> exec = new ArrayList<Executable>(
            AlgorithmExecutable.generate(Arrays.asList(algorithm)));
    assertEquals(2, exec.size());
  }

  @Test
  public void testGenerateParameters ()
  {
    HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
    parameters.put("paramA", Arrays.asList("a1", "a2"));
    parameters.put("paramB", Arrays.asList("b1"));

    algorithm.setParameters(parameters);

    HashMap<String, String> config1 = new HashMap<String, String>();
    HashMap<String, String> config2 = new HashMap<String, String>();

    algorithm.setRunPath("{paramA} {paramB}");

    config1.put("paramA", "a1");
    config1.put("paramB", "b1");

    config2.put("paramA", "a2");
    config2.put("paramB", "b1");

    List<Executable> exec = new ArrayList<Executable>(
            AlgorithmExecutable.generate(Arrays.asList(algorithm)));

    Map<String, String> params1 = exec.get(0).getParameters();
    Map<String, String> params2 = exec.get(1).getParameters();

    assertTrue(params1.equals(config1) || params1.equals(config2));
    assertTrue(params2.equals(config1) || params2.equals(config2));
    assertTrue(!params1.equals(params2));
  }

  @Test
  public void testGetParametersAsString ()
  {
    HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
    parameters.put("paramA", Arrays.asList("a1", "a2"));
    parameters.put("paramB", Arrays.asList("b1"));

    algorithm.setParameters(parameters);
    algorithm.setRunPath("{paramA} {paramB}");

    String config1 = "paramA: a1;paramB: b1;";
    String config2 = "paramA: a2;paramB: b1;";

    List<Executable> exec = new ArrayList<Executable>(
            AlgorithmExecutable.generate(Arrays.asList(algorithm)));

    String params1 = exec.get(0).getParametersAsString();
    String params2 = exec.get(1).getParametersAsString();

    assertTrue(params1.equals(config1) || params1.equals(config2));
    assertTrue(params2.equals(config1) || params2.equals(config2));
    assertTrue(!params1.equals(params2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTokenNotDefinedException ()
  {
    algorithm.setRunPath("cmd1 {param1} | cmd2 {param2}");

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");

    executable.setParameters(params);

    executable.getFullRunPath();

  }

  @Test
  public void testMoreParametersThanTokens ()
  {
    algorithm.setRunPath("cmd1 {param1} | cmd2 {param2}");

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");
    params.put("param2", "val2");
    params.put("param3", "val3");

    executable.setParameters(params);

    assertEquals("cmd1 val1 | cmd2 val2", executable.getFullRunPath());

  }

  @Test
  public void testExecutableParametersFromRunPathSetter ()
  {
    algorithm.setRunPath("cmd1 {param1} | cmd2 {param2}");

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");
    params.put("param2", "val2");
    params.put("param3", "val3");

    executable.setParameters(params);

    assertEquals(2, executable.getParameters().size());
    assertTrue(executable.getParameters().containsKey("param1"));
    assertTrue(executable.getParameters().containsKey("param2"));
  }

  @Test
  public void testExecutableParametersFromRunPathSetterNull ()
  {
    executable.setParameters(null);
    assertTrue(executable.getParameters().isEmpty());
  }

  @Test
  public void testExecutableParametersFromRunPathSetterEmpty ()
  {
    executable.setParameters(new HashMap<String, String>());
    assertTrue(executable.getParameters().isEmpty());
  }

  @Test
  public void testExecutableParametersFromRunPathConstructor ()
  {
    algorithm.setRunPath("cmd1 {param1} | cmd2 {param2}");

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");
    params.put("param2", "val2");
    params.put("param3", "val3");

    Executable exec = new AlgorithmExecutable(algorithm, params);

    assertEquals(2, exec.getParameters().size());
    assertTrue(exec.getParameters().containsKey("param1"));
    assertTrue(exec.getParameters().containsKey("param2"));

  }
}
