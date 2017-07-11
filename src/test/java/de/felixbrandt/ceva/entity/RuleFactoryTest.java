package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.RuleFactory;
import de.felixbrandt.support.ParameterMap;

public class RuleFactoryTest
{

  RuleFactory rule_factory;
  Rule rule;

  class TestRuleFactory extends RuleFactory
  {
    @Override
    public Rule doCreate ()
    {
      return new TestRule();
    }
  };

  class TestRule extends Rule
  {
    private static final long serialVersionUID = 1L;

    @Override
    public HashMap<String, List<String>> getParameters ()
    {
      return new HashMap<String, List<String>>();
    }
  };

  @Before
  public void setup ()
  {
    rule_factory = new TestRuleFactory();
    rule = new TestRule();

    rule.setName("TestRule");
  }

  @Test
  public void testEmptyVersion ()
  {
    HashMap<String, Integer> params = new HashMap<String, Integer>();
    rule_factory.init(rule, new ParameterMap(params));
    assertEquals("", rule.getFullVersionPath());
  }

  @Test
  public void testFixedVersion ()
  {
    HashMap<String, Integer> params = new HashMap<String, Integer>();
    params.put("version", 42);

    rule_factory.init(rule, new ParameterMap(params));
    assertEquals("echo 42", rule.getFullVersionPath());
  }

  @Test
  public void testFlexibleVersion ()
  {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("version_path", "vpath");

    rule_factory.init(rule, new ParameterMap(params));
    assertEquals("vpath", rule.getFullVersionPath());
  }

  @Test
  public void testConsistencyRuleActiveNoBaseAndRunPath ()
  {

    rule.setActive(true);
    rule.setBasePath("");
    rule.setRunPath("");

    assertEquals(false, rule_factory.checkRuleConsistency(rule));
  }

  @Test
  public void testConsistencyRuleActiveBaseAndRunPathSet ()
  {
    rule.setActive(true);
    rule.setBasePath("test/base/path");
    rule.setRunPath("test/run/path");

    assertEquals(true, rule_factory.checkRuleConsistency(rule));
  }

  @Test
  public void testConsistencyRuleInactiveNoBaseAndRunPath ()
  {
    rule.setActive(false);
    rule.setBasePath("");
    rule.setRunPath("");

    assertEquals(true, rule_factory.checkRuleConsistency(rule));
  }

  @Test
  public void testConsistencyRuleInactiveBaseAndRunPathSet ()
  {
    rule.setActive(false);
    rule.setBasePath("test/base/path");
    rule.setRunPath("test/run/path");

    assertEquals(true, rule_factory.checkRuleConsistency(rule));
  }

  @Test
  public void testConsistencyRuleActiveEmptyToken ()
  {
    rule.setActive(true);
    rule.setRunPath("cmd {}");
    assertEquals(false, rule_factory.emptyTokenCheck(rule));
  }

  public void testConsistencyRuleActiveNonEmptyToken ()
  {
    rule.setActive(true);
    rule.setRunPath("cmd {}");
    assertEquals(true, rule_factory.emptyTokenCheck(rule));
  }

  @Test
  public void testConsistencyRuleInactiveEmptyToken ()
  {
    rule.setActive(false);
    rule.setRunPath("cmd {}");
    assertEquals(true, rule_factory.emptyTokenCheck(rule));
  }

  @Test
  public void testConsistencyRuleInactiveNonEmptyToken ()
  {
    rule.setActive(false);
    rule.setRunPath("cmd {nonempty}");
    assertEquals(true, rule_factory.emptyTokenCheck(rule));
  }

}
