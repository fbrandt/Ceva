package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.AlgorithmFactory;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.RuleFactory;
import de.felixbrandt.support.ParameterMap;

public class AlgorithmFactoryTest
{
  RuleFactory factory;
  HashMap<String, String> data;
  ParameterMap params;
  Rule base_rule;

  class MockRule extends Algorithm
  {
    private static final long serialVersionUID = 1L;
  }

  @Before
  public void setup ()
  {
    factory = new AlgorithmFactory();
    data = new HashMap<String, String>();
    params = new ParameterMap(data);
    base_rule = new MockRule();
  }

  @Test
  public void testDoCreate ()
  {
    assertEquals(Algorithm.class, factory.doCreate().getClass());
  }

  @Test
  public void testInit ()
  {
    final Rule result = factory.init(base_rule, null);
    assertEquals(base_rule, result);
  }

  @Test
  public void testInitName ()
  {
    data.put("name", "rulename");
    final Rule result = factory.init(base_rule, params);
    assertEquals("rulename", result.getName());
  }

  @Test
  public void testInitDescription ()
  {
    data.put("description", "ruledesc");
    final Rule result = factory.init(base_rule, params);
    assertEquals("ruledesc", result.getDescription());
  }

  @Test
  public void testInitBasePath ()
  {
    data.put("base_path", "mypath");
    final Rule result = factory.init(base_rule, params);
    assertEquals("mypath", result.getBasePath());
  }

  @Test
  public void testInitVersionPath ()
  {
    data.put("version_path", "mypath");
    final Rule result = factory.init(base_rule, params);
    assertEquals("mypath", result.getVersionPath());
  }

  @Test
  public void testInitRunPath ()
  {
    data.put("run_path", "mypath");
    final Rule result = factory.init(base_rule, params);
    assertEquals("mypath", result.getRunPath());
  }

  @Test
  public void testInitRepeatDefault ()
  {
    final Rule result = factory.init(base_rule, params);
    assertEquals(1, ((Algorithm) result).getRepeat());
  }

  @Test
  public void testInitRepeat ()
  {
    data.put("repeat", "2");
    final Rule result = factory.init(base_rule, params);
    assertEquals(2, ((Algorithm) result).getRepeat());
  }

  @Test
  public void testInitActiveDefault ()
  {
    final Rule result = factory.init(base_rule, params);
    assertEquals(true, result.isActive());
  }

  @Test
  public void testInitActive ()
  {
    data.put("active", "false");
    final Rule result = factory.init(base_rule, params);
    assertEquals(false, result.isActive());
  }

  @Test
  public void testInitParameterDefault ()
  {
    final Rule result = factory.init(base_rule, params);
    assertEquals(0, ((Algorithm) result).getParameters().size());
  }

  @Test
  public void testInitParameterString ()
  {
    data.put("parameters", "parameterA: a \nparameterB: a,b");
    final Rule result = factory.init(base_rule, params);
    assertEquals(2, ((Algorithm) result).getParameters().size());
    assertEquals(Arrays.asList("a"), ((Algorithm) result).getParameters().get("parameterA"));

    assertEquals(1, ((Algorithm) result).getParameters().get("parameterA").size());
    assertEquals(2, ((Algorithm) result).getParameters().get("parameterB").size());
  }

  @Test
  public void testInvalidTokenInRunpath ()
  {
    AlgorithmFactory algo_factory = new AlgorithmFactory();
    data.put("parameters", "parameterA: a");

    Algorithm algo = new Algorithm();
    algo.setRunPath("cmd1 {parameterA} | cmd2 {parameterC}");

    assertEquals(false, algo_factory.undefinedTokenCheck(algo, params));
  }

  @Test
  public void testOnlyValidTokensInRunpath ()
  {
    AlgorithmFactory algo_factory = new AlgorithmFactory();
    data.put("parameters", "parameterA: a \nparameterB: a,b");

    Algorithm algo = new Algorithm();
    algo.setRunPath("cmd1 {parameterA} | cmd2 {parameterB}");

    assertEquals(true, algo_factory.undefinedTokenCheck(algo, params));
  }
}
