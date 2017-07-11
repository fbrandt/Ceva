package de.felixbrandt.ceva.database;

import static org.junit.Assert.assertEquals;

import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.DBConfiguration;
import de.felixbrandt.ceva.database.PGConfiguration;

public class PGConfigurationTest
{

  PGConfiguration pg_config;

  @Before
  public void setUp ()
  {
    pg_config = new PGConfiguration();
  }

  @Test
  public void testGetConfigDefault ()
  {
    DBConfiguration db_config = new DBConfiguration();

    Configuration config = pg_config.getConfig(db_config);

    assertEquals("org.postgresql.Driver",
            config.getProperty("hibernate.connection.driver_class"));
    assertEquals("org.hibernate.dialect.PostgreSQLDialect",
            config.getProperty("hibernate.dialect"));
    assertEquals("update", config.getProperty("hibernate.hbm2ddl.auto"));
    assertEquals(
            "jdbc:postgresql://localhost:5432/ceva?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
            config.getProperty("hibernate.connection.url"));
    assertEquals("ceva", config.getProperty("hibernate.connection.username"));
    assertEquals("", config.getProperty("hibernate.connection.password"));

  }

  @Test
  public void testGetConfigParams ()
  {
    DBConfiguration db_config = new DBConfiguration();
    db_config.setHost("host");
    db_config.setPort(5555);
    db_config.setDatabase("dbname");
    db_config.setUsername("cevauser");
    db_config.setPassword("cevapass");

    Configuration config = pg_config.getConfig(db_config);

    assertEquals("org.postgresql.Driver",
            config.getProperty("hibernate.connection.driver_class"));
    assertEquals("org.hibernate.dialect.PostgreSQLDialect",
            config.getProperty("hibernate.dialect"));
    assertEquals("update", config.getProperty("hibernate.hbm2ddl.auto"));
    assertEquals(
            "jdbc:postgresql://host:5555/dbname?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
            config.getProperty("hibernate.connection.url"));
    assertEquals("cevauser", config.getProperty("hibernate.connection.username"));
    assertEquals("cevapass", config.getProperty("hibernate.connection.password"));

  }
}
