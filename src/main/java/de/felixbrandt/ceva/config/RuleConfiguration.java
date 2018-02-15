package de.felixbrandt.ceva.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.RuleFactory;
import de.felixbrandt.support.ParameterMap;

/**
 * Application rules (algorithms or metrics) configuration.
 *
 * Transform text tree structure into Rule objects.
 */
public class RuleConfiguration
{
  private RuleFactory rule_factory;
  private ParameterMap rules_data;

  public RuleConfiguration(final RuleFactory factory, final ParameterMap data)
  {
    this.rule_factory = factory;
    this.rules_data = data;
  }

  public final RuleFactory getRuleFactory ()
  {
    return rule_factory;
  }

  public final Rule generateRule (final String name, final ParameterMap rule_data)
  {
    final Rule rule = rule_factory.create(rule_data);
    rule.setName(name);

    return rule;
  }

  public final List<Rule> getRules ()
  {
    final List<Rule> result = new ArrayList<Rule>();
    final Iterator<String> rule_iter = rules_data.keySet().iterator();

    while (rule_iter.hasNext()) {
      final String rule_name = rule_iter.next();
      final ParameterMap rule_params = rules_data.getMapParam(rule_name);
      result.add(generateRule(rule_name, rule_params));
    }

    return result;
  }
}
