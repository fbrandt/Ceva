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

import de.felixbrandt.support.ShellCommand.ShellCommandError;
import de.felixbrandt.support.ShellCommand.ShellCommandWarning;

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
  public void testRun () throws ShellCommandError, ShellCommandWarning
  {
    final ShellStreamCommand command = new ShellStreamCommand("echo 1", stdin);
    command.run();
    assertEquals(1, Integer.parseInt(command.getStdoutString().trim()));
    assertEquals("", command.getStderrString());
    assertEquals(0, command.getExitcode());
  }

  @Test
  public void testRunFail () throws ShellCommandError, ShellCommandWarning
  {
    final ShellStreamCommand command = new ShellStreamCommand("invalidcommand", stdin);
    command.run();
    assertEquals("", command.getStdoutString());
    assertTrue(command.getStderrString().contains("invalidcommand"));
    assertNotEquals(0, command.getExitcode());
  }

  @Test(timeout = 10000)
  public void testFind () throws FileNotFoundException, ShellCommandError, ShellCommandWarning
  {
    if (!"Linux".equals(System.getProperty("os.name"))) {
      final File file = new File("test/result.log");
      FileInputStream stderr = new FileInputStream(file.getPath());
      final ShellStreamCommand command = new ShellStreamCommand(
              "C:\\WINDOWS\\System32\\find.exe /C \"Lorem ipsum\"", stderr);
      command.run();
      assertEquals(76, Integer.parseInt(command.getStdoutString().trim()));
      assertEquals("", command.getStderrString());
    }
  }

  @Test
  public void testGetStderr () throws ShellCommandError, ShellCommandWarning
  {
    final ShellStreamCommand command = new ShellStreamCommand("echo 1 >&2", stdin);
    command.run();
    assertEquals("", command.getStdoutString());
    assertEquals("1", command.getStderrString().substring(0, 1));
    assertEquals(0, command.getExitcode());
  }

  @Test
  public void testGetExitCode () throws ShellCommandError, ShellCommandWarning
  {
    final ShellStreamCommand command = new ShellStreamCommand("exit 1", stdin);
    command.run();
    assertEquals(1, command.getExitcode());
  }

  @Test(timeout = 5000)
  public void testRuntime ()
          throws InterruptedException, ShellCommandError, ShellCommandWarning
  {
    final String osname = System.getProperty("os.name");
    String count_param = "-c";

    if (osname.toLowerCase().matches("(.*)windows(.*)")) {
      count_param = "-n";
    }

    final ShellStreamCommand command = new ShellStreamCommand(
            "ping " + count_param + " 2 127.0.0.1", stdin);
    command.run();
    Thread.sleep(1000);
    assertEquals(1.0, command.getRuntime(), 0.5);
  }

}
