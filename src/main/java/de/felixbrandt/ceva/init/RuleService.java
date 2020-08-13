package de.felixbrandt.ceva.init;

import java.util.Collection;
import java.util.List;

import org.hibernate.query.Query;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.entity.RuleFactory;

/**
 * Import/Update rules into database.
 */
public class RuleService<RuleType>
{
  private SessionHandler session_handler;
  private RuleFactory rule_factory;
  private String table_name;

  public RuleService(final SessionHandler handler, final RuleFactory factory, final String table)
  {
    session_handler = handler;
    rule_factory = factory;
    table_name = table;
  }

  public final Collection<RuleType> getActive ()
  {
    return session_handler.getSession().createQuery("from " + table_name + " where active = true")
            .list();
  }

  public final Collection<RuleType> getAll ()
  {
    return session_handler.getSession().createQuery("from " + table_name).list();
  }

  public final RuleType getByName (final String name)
  {
    final Query query = session_handler.getSession().createQuery(
            "from " + table_name + " where name = :name");
    query.setParameter("name", name);

    final List<?> result = query.list();
    if (result.size() > 0) {
      return (RuleType) result.get(0);
    }

    return null;
  }

  public final void update (final Collection<Rule> rules)
  {
    for (final Rule rule : rules) {
      update(rule);
    }
  }

  public final void update (final Rule rule_update)
  {
    Rule current_rule = (Rule) getByName(rule_update.getName());

    if (current_rule != null) {
      current_rule.updateFrom(rule_update);
    } else {
      current_rule = create().updateFrom(rule_update);
    }

    session_handler.getSession().save(current_rule);
  }

  public final Rule create ()
  {
    return rule_factory.create();
  }
}
