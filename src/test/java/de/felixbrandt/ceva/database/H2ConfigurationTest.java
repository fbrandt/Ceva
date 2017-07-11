package de.felixbrandt.ceva.database;

import static org.junit.Assert.assertEquals;

import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.DBConfiguration;
import de.felixbrandt.ceva.database.H2Configuration;

public class H2ConfigurationTest
{

  H2Configuration h2_config;

  @Before
  public void setUp ()
  {
    h2_config = new H2Configuration();
  }

  @Test
  public void testGetConfigDefault ()
  {
    DBConfiguration db_config = new DBConfiguration();

    Configuration config = h2_config.getConfig(db_config);

    assertEquals("org.h2.Driver", config.getProperty("hibernate.connection.driver_class"));
    assertEquals("org.hibernate.dialect.H2Dialect", config.getProperty("hibernate.dialect"));
    assertEquals("jdbc:h2:./ceva", config.getProperty("hibernate.connection.url"));
  }

  @Test
  public void testGetConfigParams ()
  {
    DBConfiguration db_config = new DBConfiguration();
    db_config.setDatabase("dbname");

    Configuration config = h2_config.getConfig(db_config);

    assertEquals("jdbc:h2:./dbname", config.getProperty("hibernate.connection.url"));
  }
}
