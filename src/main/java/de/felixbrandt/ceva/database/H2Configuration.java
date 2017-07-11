package de.felixbrandt.ceva.database;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.cfg.Configuration;

import de.felixbrandt.ceva.config.DBConfiguration;

/**
 * Add configuration to connect to a local H2 database.
 */
public class H2Configuration implements DatabaseConfiguration
{
  private static Logger logger;
  private String handle_existing;

  public H2Configuration()
  {
    handle_existing = "update";
  }

  /**
   * Howto handle existing schemas:
   * <ul>
   * <li>\c create destroy previous data (used for unit testing)</li>
   * <li>\c update update schema (default)</li>
   * <li>\c validate check schema, make no changes</li>
   * </ul>
   */
  public H2Configuration(final String _handle_existing)
  {
    handle_existing = _handle_existing;
  }

  public static synchronized void setupLogger ()
  {
    if (logger == null) {
      logger = Logger.getLogger("org.hibernate");
      logger.setLevel(Level.SEVERE);
    }
  }

  public final Configuration getConfig (final DBConfiguration db_config)
  {
    Configuration result = new Configuration();
    setupLogger();

    result.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    result.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
    result.setProperty("hibernate.connection.url", "jdbc:h2:./" + db_config.getDatabase());
    result.setProperty("hibernate.hbm2ddl.auto", handle_existing);

    return result;
  }
}
