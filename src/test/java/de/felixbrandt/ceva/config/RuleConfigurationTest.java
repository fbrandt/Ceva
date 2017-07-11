package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.RuleConfiguration;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.AlgorithmFactory;
import de.felixbrandt.ceva.entity.Rule;

public class RuleConfigurationTest
{
  Map<String, String> rule;
  Map<String, Map<String, String>> rule_map;
  RuleConfiguration rule_config;

  @Before
  public void setUp () throws Exception
  {
    rule = new HashMap<String, String>();
    rule.put("description", "ruledesc");
    rule_map = new HashMap<String, Map<String, String>>();
    rule_map.put("rulekey", rule);

    rule_config = new RuleConfiguration(new AlgorithmFactory(), rule_map);
  }

  @Test
  public void testGenerateRule ()
  {
    final Rule result = rule_config.generateRule("rulekey", rule);
    assertEquals(Algorithm.class, result.getClass());
    assertEquals("rulekey", result.getName());
    assertEquals("ruledesc", result.getDescription());
  }

  @Test
  public void testGenerateRules ()
  {
    final List<Rule> rules = rule_config.getRules();
    assertEquals(1, rules.size());
    assertEquals("rulekey", rules.get(0).getName());
  }
}
