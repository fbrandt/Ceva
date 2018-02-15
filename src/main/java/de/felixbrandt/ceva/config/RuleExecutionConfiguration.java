package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

public class RuleExecutionConfiguration
{
  private boolean is_active;

  public RuleExecutionConfiguration()
  {
    this(true);
  }

  public RuleExecutionConfiguration(boolean is_active)
  {
    this(is_active, null);
  }

  public RuleExecutionConfiguration(boolean is_active, ParameterMap params)
  {
    this.is_active = is_active;
  }

  public boolean isActive ()
  {
    return is_active;
  }

}
