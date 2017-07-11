package de.felixbrandt.ceva.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
  private Map<String, ?> rules_data;

  public RuleConfiguration(final RuleFactory factory, final Map<String, ?> data)
  {
    this.rule_factory = factory;
    this.rules_data = data;
  }

  public final RuleFactory getRuleFactory ()
  {
    return rule_factory;
  }

  public final Rule generateRule (final String name, final Map<String, ?> rule_data)
  {
    final Rule rule = rule_factory.create(new ParameterMap(rule_data));
    rule.setName(name);

    return rule;
  }

  public final List<Rule> getRules ()
  {
    final List<Rule> result = new ArrayList<Rule>();
    final Iterator<?> rule_iter = rules_data.entrySet().iterator();

    while (rule_iter.hasNext()) {
      final Map.Entry entry = (Map.Entry) rule_iter.next();
      final String rule_name = (String) entry.getKey();
      final Map<String, ?> rule_params = (Map<String, ?>) entry.getValue();
      result.add(generateRule(rule_name, rule_params));
    }

    return result;
  }
}
