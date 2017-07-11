package de.felixbrandt.ceva.database;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Add configuration to connect to a PostgreSQL database.
 */
public class PGConfiguration implements DatabaseConfiguration
{
  public static final int PG_DEFAULT_PORT = 5432;

  public PGConfiguration()
  {
  }

  public final Configuration getConfig (final DBConfiguration db_config)
  {
    Configuration result = new Configuration();

    int port = db_config.getPort();
    if (port == DBConfiguration.DEFAULT_PORT) {
      port = PG_DEFAULT_PORT;
    }

    result.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
    result.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    result.setProperty("hibernate.hbm2ddl.auto", "update");
    result.setProperty("hibernate.connection.url",
            "jdbc:postgresql://" + db_config.getHost() + ":" + port + "/"
                    + db_config.getDatabase()
                    + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
    result.setProperty("hibernate.connection.username", db_config.getUsername());
    result.setProperty("hibernate.connection.password", db_config.getPassword());

    return result;
  }
}
