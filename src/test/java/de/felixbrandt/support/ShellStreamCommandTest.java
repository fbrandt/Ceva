package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ShellStreamCommand;
import de.felixbrandt.support.StreamSupport;

public class ShellStreamCommandTest
{

  InputStream stdin;

  @Before
  public void setup ()
  {
    stdin = StreamSupport.createEmptyInputStream();
  }

  @Test
  public void testGetLinuxPrefix ()
  {
    final Vector<String> prefix = ShellStreamCommand.getOSPrefix("Linux");
    assertEquals("/bin/bash", prefix.get(0));
    assertEquals("-c", prefix.get(1));
  }

  @Test
  public void testGetWindowsPrefix ()
  {
    final Vector<String> prefix = ShellStreamCommand.getOSPrefix("Windows 7");
    assertEquals("cmd", prefix.get(0));
    assertEquals("/c", prefix.get(1));
  }

  @Test
  public void testRun ()
  {
    final Command command = new ShellStreamCommand("echo 1", stdin);
    assertEquals(1, Integer.parseInt(command.getStdoutString().trim()));
    assertEquals("", command.getStderrString());
    assertEquals(0, command.getExitCode());
  }

  @Test
  public void testRunFail ()
  {
    final Command command = new ShellStreamCommand("invalidcommand", stdin);
    assertEquals("", command.getStdoutString());
    assertTrue(command.getStderrString().contains("invalidcommand"));
    assertNotEquals(0, command.getExitCode());
  }

  @Test(timeout = 10000)
  public void testFind () throws FileNotFoundException
  {
    if (!"Linux".equals(System.getProperty("os.name"))) {
      final File file = new File("test/result.log");
      FileInputStream stderr = new FileInputStream(file.getPath());
      final Command command = new ShellStreamCommand(
              "C:\\WINDOWS\\System32\\find.exe /C \"Lorem ipsum\"", stderr);
      assertEquals(76, Integer.parseInt(command.getStdoutString().trim()));
      assertEquals("", command.getStderrString());
    }
  }

  @Test
  public void testGetStderr ()
  {
    final Command command = new ShellStreamCommand("echo 1 >&2", stdin);
    assertEquals("", command.getStdoutString());
    assertEquals("1", command.getStderrString().substring(0, 1));
    assertEquals(0, command.getExitCode());
  }

  @Test
  public void testGetExitCode ()
  {
    final Command command = new ShellStreamCommand("exit 1", stdin);
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

    final Command command = new ShellStreamCommand("ping " + count_param + " 2 127.0.0.1",
            stdin);
    Thread.sleep(1000);
    assertEquals(1.0, command.getRuntime(), 0.5);
  }

}
