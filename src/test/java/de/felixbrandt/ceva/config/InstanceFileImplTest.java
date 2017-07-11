package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class InstanceFileImplTest
{
  InstanceFile no_file;
  InstanceFile file;

  @Before
  public void setup ()
  {
    no_file = new InstanceFileImpl("file/does/not/exists.txt");
    file = new InstanceFileImpl("src/main/java/de/felixbrandt/ceva/config/InstanceFile.java");
  }

  @Test
  public void testNotReadable ()
  {
    assertFalse(no_file.isReadable());
  }

  @Test
  public void testReadable ()
  {
    assertTrue(file.isReadable());
  }

  @Test
  public void testFilename ()
  {
    assertEquals("exists.txt", no_file.getFilename());
    assertEquals("InstanceFile.java", file.getFilename());
  }

  @Test
  public void testContent ()
  {
    assertEquals("", no_file.getContent());
    assertTrue(file.getContent().length() >= 100);
  }

  @Test
  public void testHash ()
  {
    assertEquals(32, file.getHash().length());
  }
}
