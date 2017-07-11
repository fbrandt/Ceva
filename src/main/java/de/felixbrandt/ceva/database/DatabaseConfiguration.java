package de.felixbrandt.ceva.database;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Factory for creating Hibernate Configuration.
 */
public interface DatabaseConfiguration
{
  Configuration getConfig (final DBConfiguration db_config);
}
