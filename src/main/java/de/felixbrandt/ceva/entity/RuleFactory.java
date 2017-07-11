package de.felixbrandt.ceva.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.support.ParameterMap;

/**
 * Factory to setup rule entities.
 */
public abstract class RuleFactory
{

  private static final Logger LOGGER = LogManager.getLogger();

  public final Rule create ()
  {
    return create(null);
  }

  public final Rule create (final ParameterMap params)
  {
    return init(doCreate(), params);
  }

  public abstract Rule doCreate ();

  public final Rule init (final Rule rule)
  {
    return init(rule, null);
  }

  public final Rule init (final Rule rule, final ParameterMap params)
  {
    if (params != null) {
      rule.setName(params.getStringParam("name"));
      rule.setDescription(params.getStringParam("description"));
      rule.setBasePath(params.getStringParam("base_path"));
      rule.setVersionPath(params.getStringParam("version_path"));
      if (rule.getVersionPath().equals("") && params.has("version")) {
        rule.setVersionPath("echo " + Integer.toString(params.getIntParam("version")));
      }
      rule.setRunPath(params.getStringParam("run_path"));
      rule.setActive(params.getBoolParam("active", true));

      checkRuleConsistency(rule);
    }
    initRuleDetails(rule, params);

    return rule;
  }

  public final boolean checkRuleConsistency (final Rule rule)
  {
    boolean base_and_runpath_not_both_empty = baseAndRunpathConsistent(rule);
    boolean no_empty_tokens_in_runpath = emptyTokenCheck(rule);

    boolean consistent = base_and_runpath_not_both_empty && no_empty_tokens_in_runpath;

    return consistent;
  }

  public final boolean emptyTokenCheck (final Rule rule)
  {
    if (rule.isActive()) {
      boolean empty_tokens = rule.getFullRunPath().contains("{}");
      if (empty_tokens) {
        LOGGER.error("For rule " + rule.getName() + ": run_path contains empty token: {}");
        return false;
      }
    }
    return true;

  }

  public final boolean baseAndRunpathConsistent (final Rule rule)
  {

    if (rule.isActive()) {
      boolean consistent = !(rule.getBasePath().equals("") && rule.getRunPath().equals(""));
      if (!consistent) {
        LOGGER.error(
                "For rule " + rule.getName() + ": Neither base_path nor run_path was set.");
      }

      return consistent;
    } else {
      return true;
    }
  }

  public void initRuleDetails (final Rule rule, final ParameterMap params)
  {

  }
}
