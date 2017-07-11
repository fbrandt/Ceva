package de.felixbrandt.ceva.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.config.InstanceFile;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.init.InstanceImportService;

public class InstanceImportServiceTest
{
  SessionHandler session_handler;
  InstanceImportService service;

  class MockInstanceFile implements InstanceFile
  {
    public MockInstanceFile(final String hash)
    {
      this.hash = hash;
    }

    public boolean isReadable ()
    {
      return true;
    }

    public String hash;

    public String getHash ()
    {
      return hash;
    }

    public String getContent ()
    {
      return "mycontent";
    }

    public String getFilename ()
    {
      return "mockfile.txt";
    }
  }

  @Before
  public void setUp ()
  {
    session_handler = TestSessionBuilder.build();
    service = new InstanceImportService(session_handler);
  }

  @After
  public void tearDown ()
  {
    session_handler.shutdown();
  }

  @Test
  public void testInstanceExists ()
  {
    final Instance instance = new Instance();
    instance.setChecksum("ABCDEF");
    session_handler.getSession().save(instance);

    final InstanceFile instance_file = new MockInstanceFile("ABCDEF");
    assertTrue(service.instanceExists(instance_file));

    final InstanceFile other_file = new MockInstanceFile("123456");
    assertFalse(service.instanceExists(other_file));
  }

  @Test
  public void testCreateInstance ()
  {
    final InstanceFile instance_file = new MockInstanceFile("ABCDEF");
    final Instance instance = service.createInstance(instance_file);
    assertEquals("mockfile.txt", instance.getName());
    assertEquals("ABCDEF", instance.getChecksum());
    assertEquals("mycontent", instance.getContent());
  }

  @Test
  public void testImportInstanceFile ()
  {
    final InstanceFile instance_file = new MockInstanceFile("INSTANCE");
    final Instance instance = service.importInstanceFile(instance_file);
    assertNotEquals(0, instance.getInstance());

    // no second import
    assertNull(service.importInstanceFile(instance_file));
  }
}
