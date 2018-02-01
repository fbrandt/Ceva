package de.felixbrandt.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Command execution in a windows command shell using files for stdout and stderr streams.
 */
public class ShellFileCommand extends ShellCommand
{
  private String command;
  private InputStream stdin;
  private String stdout;
  private String stderr;

  public ShellFileCommand(final String _command, final InputStream _stdin)
  {
    command = _command;
    stdin = _stdin;
  }

  public void run () throws ShellCommandError, ShellCommandWarning
  {
    Process process;

    try {
      File stdout_file = File.createTempFile("ceva", ".stdout");
      File stderr_file = File.createTempFile("ceva", ".stderr");

      final Vector<String> full_command = getOSPrefix(System.getProperty("os.name"));
      full_command.add(command + " >" + stdout_file.getAbsolutePath() + " 2>"
              + stderr_file.getAbsolutePath());

      final String[] c = full_command.toArray(new String[full_command.size()]);

      process = startProcess(c);

      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(stdin, process.getOutputStream());
      stdin_stream.close();

      waitForProcess();

      stdout = StreamSupport.getStringFromInputStream(new FileInputStream(stdout_file));
      stderr = StreamSupport.getStringFromInputStream(new FileInputStream(stderr_file));

      if (!stdout_file.delete() || !stderr_file.delete()) {
        throw new ShellCommandWarning("could not delete output files: " + stdout_file.getName()
                + " or " + stderr_file.getName());
      }
    } catch (final IOException e) {
      process = null;
      throw new ShellCommandError("command failed with: " + e.getMessage());
    } catch (final InterruptedException e) {
      process = null;
      throw new ShellCommandError("command interrupted: " + e.getMessage());
    }
  }

  public final String getStdoutString ()
  {
    if (stdout != null) {
      return stdout;
    }

    return "";
  }

  public final String getStderrString ()
  {
    if (stderr != null) {
      return stderr;
    }

    return "";
  }
}
