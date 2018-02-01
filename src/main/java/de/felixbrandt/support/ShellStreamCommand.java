package de.felixbrandt.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * [DEPRECATED] Command execution in a windows command shell using the input/stdout/stderr
 * streams.
 *
 * This is deprecated, because of performance issues with the output streams. Java blocks the
 * external process if the output streams cannot be processed fast enough.
 */
public class ShellStreamCommand extends ShellCommand
{
  private StreamedString stdout;
  private StreamedString stderr;

  public ShellStreamCommand(final String command, final InputStream stdin)
  {
    super(command, stdin);
  }

  public void run () throws ShellCommandError
  {
    Process process = null;

    try {
      final Vector<String> full_command = getOSPrefix(System.getProperty("os.name"));
      full_command.add(getCommand());

      final String[] c = full_command.toArray(new String[full_command.size()]);

      process = startProcess(c);

      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(getStdin(), process.getOutputStream());
      stdin_stream.close();

      stdout = StreamedString.create(process.getInputStream());
      stderr = StreamedString.create(process.getErrorStream());

      waitForProcess();

      stdout.join();
      stderr.join();
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
      return stdout.getString();
    }

    return "";
  }

  public final String getStderrString ()
  {
    if (stderr != null) {
      return stderr.getString();
    }

    return "";
  }
}
