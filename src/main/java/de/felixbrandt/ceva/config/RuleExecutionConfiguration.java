package de.felixbrandt.ceva.config;

import java.util.List;

import de.felixbrandt.support.ParameterMap;

public class RuleExecutionConfiguration
{
  private boolean is_active;
  private ParameterMap params;

  public RuleExecutionConfiguration()
  {
    this(true);
  }

  public RuleExecutionConfiguration(boolean is_active)
  {
    this(is_active, new ParameterMap());
  }

  public RuleExecutionConfiguration(boolean is_active, ParameterMap params)
  {
    this.is_active = is_active;
    this.params = params;
  }

  public boolean isActive ()
  {
    return is_active;
  }

  public List<String> getWhitelist ()
  {
    return (List<String>) params.getListParam("include", null);
  }

  public List<String> getBlacklist ()
  {
    return (List<String>) params.getListParam("exclude", null);
  }

}
