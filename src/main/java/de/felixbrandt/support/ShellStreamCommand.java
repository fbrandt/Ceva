package de.felixbrandt.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;

/**
 * [DEPRECATED] Command execution in a windows command shell using the input/stdout/stderr
 * streams.
 *
 * This is deprecated, because Java blocks the external process if the output streams cannot be
 * processed fast enough. On machines running 4 workers, the runtimes of the workers was
 * sometimes doubled, compared to runs outside CEVA.
 */
public class ShellStreamCommand extends ShellCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();
  private StreamedString stdout;
  private StreamedString stderr;

  public ShellStreamCommand(final String command, final InputStream stdin)
  {
    Process process = null;

    try {
      final Vector<String> full_command = getOSPrefix(System.getProperty("os.name"));
      full_command.add(command);
      LOGGER.debug("running command: {}", full_command);

      final String[] c = full_command.toArray(new String[full_command.size()]);

      process = startProcess(c);

      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(stdin, process.getOutputStream());
      stdin_stream.close();

      stdout = StreamedString.create(process.getInputStream());
      stderr = StreamedString.create(process.getErrorStream());

      waitForProcess();

      stdout.join();
      stderr.join();
    } catch (final IOException e) {
      LOGGER.error("command failed with: " + e.getMessage());
      process = null;
    } catch (final InterruptedException e) {
      LOGGER.error("command interrupted: " + e.getMessage());
      process = null;
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
