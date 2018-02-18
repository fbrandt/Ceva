package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.support.ParameterMap;

public class RuleExecutionConfigurationTest
{
  List<String> list;
  Map<String, Object> data;
  ParameterMap params;
  RuleExecutionConfiguration config;

  @Before
  public void setup ()
  {
    list = new ArrayList<String>();
    list.add("test");
    data = new HashMap<String, Object>();
    params = new ParameterMap(data);
    config = new RuleExecutionConfiguration(true, params);
  }

  @Test
  public void testDefault ()
  {
    assertEquals(null, config.getWhitelist());
    assertEquals(null, config.getBlacklist());
  }

  @Test
  public void testWhitelist ()
  {
    data.put("include", list);
    assertEquals(list, config.getWhitelist());
  }

  @Test
  public void testBlacklist ()
  {
    data.put("exclude", list);
    assertEquals(list, config.getBlacklist());
  }

  @Test
  public void testDecorator ()
  {
    Map<String, String> deco = new HashMap<String, String>();
    deco.put("run_path", "echo test");
    deco.put("version", "42");
    data.put("include", list);
    data.put("other", deco);

    ParameterMap decorators = config.getDecorators();
    assertEquals(1, decorators.size());
    assertTrue(decorators.has("other"));
  }
}
