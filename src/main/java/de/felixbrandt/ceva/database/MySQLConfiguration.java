package de.felixbrandt.ceva.database;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Add configuration to connect to a PostgreSQL database.
 */
public class MySQLConfiguration implements DatabaseConfiguration
{
  public static final int DEFAULT_PORT = 3306;

  public MySQLConfiguration()
  {
  }

  public final Configuration getConfig (final DBConfiguration db_config)
  {
    Configuration result = new Configuration();

    int port = db_config.getPort();
    if (port == DBConfiguration.DEFAULT_PORT) {
      port = DEFAULT_PORT;
    }

    result.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
    result.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    result.setProperty("hibernate.hbm2ddl.auto", "update");
    result.setProperty("hibernate.connection.url",
            "jdbc:mysql://" + db_config.getHost() + ":" + port + "/" + db_config.getDatabase()
                    + "?useSSL=true&verifyServerCertificate=false");
    result.setProperty("hibernate.connection.username", db_config.getUsername());
    result.setProperty("hibernate.connection.password", db_config.getPassword());

    return result;
  }
}
