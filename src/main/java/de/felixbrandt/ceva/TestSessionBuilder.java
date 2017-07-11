package de.felixbrandt.ceva;

import de.felixbrandt.ceva.config.DBConfiguration;
import de.felixbrandt.ceva.database.H2Configuration;
import de.felixbrandt.ceva.database.SessionHandler;

/**
 * Create a H2DB CEVA SessionHandler (used for unit testing).
 */
public final class TestSessionBuilder
{
  /** Class cannot be instantiated */
  private TestSessionBuilder()
  {
  }

  public static SessionHandler build ()
  {
    DBConfiguration db_config = new DBConfiguration();
    db_config.setDatabase("test");
    return CevaSessionBuilder.build(new H2Configuration("create").getConfig(db_config));
  }
}
