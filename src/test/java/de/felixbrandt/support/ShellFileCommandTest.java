package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.support.ShellCommand.ShellCommandError;
import de.felixbrandt.support.ShellCommand.ShellCommandWarning;

public class ShellFileCommandTest
{

  InputStream stdin;

  @Before
  public void setup ()
  {
    stdin = StreamSupport.createEmptyInputStream();
  }

  @Test
  public void testRun () throws ShellCommandError, ShellCommandWarning
  {
    final ShellFileCommand command = new ShellFileCommand("echo 1", stdin);
    command.run();
    assertEquals(1, Integer.parseInt(command.getStdoutString().trim()));
    assertEquals("", command.getStderrString());
    assertEquals(0, command.getExitCode());
  }

  @Test
  public void testRunFail ()
  {
    final ShellFileCommand command = new ShellFileCommand("invalidcommand", stdin);
    try {
      command.run();
    } catch (Exception e) {
      // do nothing
    }

    assertEquals("", command.getStdoutString());
    assertTrue(command.getStderrString().contains("invalidcommand"));
    assertNotEquals(0, command.getExitCode());
  }

  @Test
  public void testGetExitCode () throws ShellCommandError, ShellCommandWarning
  {
    final ShellFileCommand command = new ShellFileCommand("exit 1", stdin);
    command.run();
    assertEquals(1, command.getExitCode());
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

    final ShellFileCommand command = new ShellFileCommand(
            "ping " + count_param + " 2 127.0.0.1", stdin);
    command.run();
    assertEquals(1.0, command.getRuntime(), 0.5);

    // check that runtime does not change later
    Thread.sleep(1000);
    assertEquals(1.0, command.getRuntime(), 0.5);
  }

  @Test(timeout = 5000)
  public void testTimeout ()
          throws InterruptedException, ShellCommandError, ShellCommandWarning
  {
    final String osname = System.getProperty("os.name");
    String count_param = "-c";

    if (osname.toLowerCase().matches("(.*)windows(.*)")) {
      count_param = "-n";
    }

    final ShellFileCommand command = new ShellFileCommand(
            "ping " + count_param + " 10 127.0.0.1", stdin, 3);
    try {
      command.run();
      fail();
    } catch (ShellCommandWarning e) {
      assertTrue(e.getMessage().matches("(.*)timelimit exceeded(.*)"));
    }

    // use same exit code as linux timeout command
    assertEquals(ShellCommand.TIMELIMIT_EXCEEDED, command.getExitCode());
    assertEquals(3.0, command.getRuntime(), 0.5);
  }
}
