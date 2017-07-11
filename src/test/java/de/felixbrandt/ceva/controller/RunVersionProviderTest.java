package de.felixbrandt.ceva.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockCommand;
import de.felixbrandt.ceva.controller.MockCommandFactory;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.controller.RunVersionProvider;

public class RunVersionProviderTest
{
  MockCommand command;
  MockCommandFactory factory;
  RunVersionProvider provider;
  MockExecutable executable;

  @Before
  public void setUp () throws Exception
  {
    command = new MockCommand("42", "", 0);
    factory = new MockCommandFactory(command);
    provider = new RunVersionProvider(factory);

    executable = new MockExecutable();
    executable.setVersionPath("VERSIONPATH");
  }

  @Test
  public void testGetVersion ()
  {
    assertEquals(42, provider.getVersion(executable));
    assertEquals("VERSIONPATH", factory.getLastCommandString());
    assertEquals("", factory.getLastInput());
  }

  @Test
  public void testGetVersionWhitespace ()
  {
    command.setStdout("\n23\n");
    assertEquals(23, provider.getVersion(executable));
  }

  @Test
  public void testGetVersionFail ()
  {
    command.setStdout("not a number string");
    assertEquals(-1, provider.getVersion(executable));
  }

  @Test
  public void testGetVersionDefault ()
  {
    executable.setVersionPath("");
    assertEquals(0, provider.getVersion(executable));
  }

  @Test
  public void testGetVersionNull ()
  {
    executable.setVersionPath(null);
    assertEquals(0, provider.getVersion(executable));
  }
}
