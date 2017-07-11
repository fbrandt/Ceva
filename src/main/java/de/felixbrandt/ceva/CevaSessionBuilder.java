package de.felixbrandt.ceva;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.HibernateConfiguration;

/**
 * Create a fully configured CEVA SessionHandler.
 */
public final class CevaSessionBuilder
{
  /** Class cannot be instantiated */
  private CevaSessionBuilder()
  {
  }

  /**
   * @param cfg Hibernate configuration settings
   */
  public static SessionHandler build (final Configuration cfg)
  {
    HibernateConfiguration.getConfig(cfg);

    return new SessionHandler(cfg);
  }
}
