package de.felixbrandt.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;

/**
 * Command execution in a windows command shell using files for stdout and stderr streams.
 */
public class ShellFileCommand extends ShellCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();
  private String stdout;
  private String stderr;

  public ShellFileCommand(final String command, final InputStream stdin)
  {
    Process process;

    try {
      File stdout_file = File.createTempFile("ceva", ".stdout");
      File stderr_file = File.createTempFile("ceva", ".stderr");

      final Vector<String> full_command = getOSPrefix(System.getProperty("os.name"));
      full_command.add(command + " >" + stdout_file.getAbsolutePath() + " 2>"
              + stderr_file.getAbsolutePath());
      LOGGER.debug("running command: {}", full_command);

      final String[] c = full_command.toArray(new String[full_command.size()]);

      process = startProcess(c);

      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(stdin, process.getOutputStream());
      stdin_stream.close();

      waitForProcess();

      stdout = StreamSupport.getStringFromInputStream(new FileInputStream(stdout_file));
      stderr = StreamSupport.getStringFromInputStream(new FileInputStream(stderr_file));

      if (!stdout_file.delete()) {
        LOGGER.warn("could not delete stdout file: {}", stdout_file.getName());
      }
      if (!stderr_file.delete()) {
        LOGGER.warn("could not delete stderr file: {}", stderr_file.getName());
      }
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
