package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.DBConfiguration;
import de.felixbrandt.support.ParameterMap;

public class DBConfigurationTest
{

  Map<String, String> data;
  ParameterMap params;
  DBConfiguration config;

  @Before
  public void setup ()
  {
    data = new HashMap<String, String>();
    params = new ParameterMap(data);
    config = new DBConfiguration(params);
  }

  @Test
  public void testDefault ()
  {
    final DBConfiguration c = new DBConfiguration();
    assertEquals("localhost", c.getHost());
  }

  @Test
  public void testDefaultHost ()
  {
    assertEquals("localhost", config.getHost());
  }

  @Test
  public void testDefaultPort ()
  {
    assertEquals(DBConfiguration.DEFAULT_PORT, config.getPort());
  }

  @Test
  public void testDefaultName ()
  {
    assertEquals("ceva", config.getDatabase());
  }

  @Test
  public void testDefaultUsername ()
  {
    assertEquals("ceva", config.getUsername());
  }

  @Test
  public void testDefaultPassword ()
  {
    assertEquals("", config.getPassword());
  }

  @Test
  public void testInitHost ()
  {
    data.put("host", "example.org");
    config = new DBConfiguration(params);
    assertEquals("example.org", config.getHost());
  }

  @Test
  public void testInitPort ()
  {
    data.put("port", "5555");
    config = new DBConfiguration(params);
    assertEquals(5555, config.getPort());
  }

  @Test
  public void testInitName ()
  {
    data.put("name", "database");
    config = new DBConfiguration(params);
    assertEquals("database", config.getDatabase());
  }

  @Test
  public void testInitUsername ()
  {
    data.put("username", "jondoe");
    config = new DBConfiguration(params);
    assertEquals("jondoe", config.getUsername());
  }

  @Test
  public void testInitPassword ()
  {
    data.put("password", "secret");
    config = new DBConfiguration(params);
    assertEquals("secret", config.getPassword());
  }
}
