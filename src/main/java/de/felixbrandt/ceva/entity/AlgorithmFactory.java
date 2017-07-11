package de.felixbrandt.ceva.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.support.ParameterMap;
import de.felixbrandt.support.StringSupport;

/**
 * Setup an Algorithm instance from ParameterMap
 */
public class AlgorithmFactory extends RuleFactory
{
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public final Rule doCreate ()
  {
    return new Algorithm();
  }

  @Override
  public final void initRuleDetails (final Rule rule, final ParameterMap params)
  {
    if (rule instanceof Algorithm) {
      final Algorithm algo = (Algorithm) rule;
      initRepeat(algo, params);
      initParameters(algo, params);

      if (params != null) {
        checkAlgoConsistency(algo, params);
      }
    }
  }

  public final void initRepeat (final Algorithm algo, final ParameterMap params)
  {
    if (params != null) {
      algo.setRepeat(params.getIntParam("repeat", 1));
    }
  }

  public final void initParameters (final Algorithm algo, final ParameterMap params)
  {
    if (params != null) {
      algo.setParameters((HashMap<String, List<String>>) params.getMapParam("parameters"));
    }

  }

  public final boolean checkAlgoConsistency (final Algorithm algo, final ParameterMap params)
  {
    boolean undefined_tokens = undefinedTokenCheck(algo, params);

    return undefined_tokens;
  }

  public final boolean undefinedTokenCheck (final Algorithm algo, final ParameterMap params)
  {
    Set<String> undefined_tokens = StringSupport.findUndefinedTokens(algo.getFullRunPath(),
            params.getMapParam("parameters").keySet());

    for (String token : undefined_tokens) {
      LOGGER.error("No parameter definition found for token {" + token + "}.");
    }

    return undefined_tokens.isEmpty();
  }
}
