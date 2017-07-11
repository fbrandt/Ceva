package de.felixbrandt.ceva.database;

import java.util.HashMap;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Build Hibernate Configuration for configured DBMS.
 */
public class HibernateConfigurationBuilder
{
  private HashMap<String, DatabaseConfiguration> supported_dbms;

  public HibernateConfigurationBuilder()
  {
    supported_dbms = new HashMap<String, DatabaseConfiguration>();
    supported_dbms.put("h2", new H2Configuration());
    supported_dbms.put("pgsql", new PGConfiguration());
    supported_dbms.put("mysql", new MySQLConfiguration());
    supported_dbms.put("sqlite", new SQLiteConfiguration());
  }

  public final Configuration create (final DBConfiguration db_config) throws Exception
  {
    if (supported_dbms.containsKey(db_config.getType().toLowerCase())) {
      return supported_dbms.get(db_config.getType()).getConfig(db_config);
    }

    throw new Exception("Database " + db_config.getType() + " not supported");

  }
}
