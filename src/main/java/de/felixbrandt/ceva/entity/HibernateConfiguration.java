package de.felixbrandt.ceva.entity;

import org.hibernate.cfg.Configuration;

/**
 * Add entity classes to database configuration.
 */
public final class HibernateConfiguration
{
  /** Prevent instantiation */
  private HibernateConfiguration()
  {
  }

  public static Configuration getConfig (final Configuration cfg)
  {
    cfg.addClass(Instance.class);
    cfg.addClass(Rule.class);
    cfg.addClass(Data.class);
    cfg.addClass(Solution.class);

    return cfg;
  }
}
