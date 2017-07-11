package de.felixbrandt.ceva.database;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Add configuration to connect to a local H2 database.
 */
public class SQLiteConfiguration implements DatabaseConfiguration
{
  public final Configuration getConfig (final DBConfiguration db_config)
  {
    Configuration result = new Configuration();

    result.setProperty("hibernate.dialect",
            "com.enigmabridge.hibernate.dialect.SQLiteDialect");
    result.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
    result.setProperty("hibernate.connection.url",
            "jdbc:sqlite:" + db_config.getDatabase() + ".db");
    result.setProperty("hibernate.hbm2ddl.auto", "update");

    return result;
  }
}
