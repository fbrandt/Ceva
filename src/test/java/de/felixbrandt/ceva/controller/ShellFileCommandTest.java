package de.felixbrandt.ceva.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.ShellFileCommand;
import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

public class ShellFileCommandTest
{

  InputStream stdin;

  @Before
  public void setup ()
  {
    stdin = StreamSupport.createEmptyInputStream();
  }

  @Test
  public void testRun ()
  {
    final Command command = new ShellFileCommand("echo 1", stdin);
    assertEquals(1, Integer.parseInt(command.getStdoutString().trim()));
    assertEquals("", command.getStderrString());
    assertEquals(0, command.getExitCode());
  }

  @Test
  public void testRunFail ()
  {
    final Command command = new ShellFileCommand("invalidcommand", stdin);
    assertEquals("", command.getStdoutString());
    assertTrue(command.getStderrString().contains("invalidcommand"));
    assertNotEquals(0, command.getExitCode());
  }

  @Test
  public void testGetExitCode ()
  {
    final Command command = new ShellFileCommand("exit 1", stdin);
    assertEquals(1, command.getExitCode());
  }

  @Test(timeout = 5000)
  public void testRuntime () throws InterruptedException
  {
    final String osname = System.getProperty("os.name");
    String count_param = "-c";

    if (osname.toLowerCase().matches("(.*)windows(.*)")) {
      count_param = "-n";
    }

    final Command command = new ShellFileCommand("ping " + count_param + " 2 127.0.0.1", stdin);
    Thread.sleep(1000);
    assertEquals(1.0, command.getRuntime(), 0.5);
  }

}
