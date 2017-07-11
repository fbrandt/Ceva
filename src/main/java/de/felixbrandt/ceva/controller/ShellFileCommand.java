package de.felixbrandt.ceva.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Command execution in a windows command shell using files for stdout and stderr streams.
 */
public class ShellFileCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();
  private static final double MICROSECONDS_PER_SECOND = 1000.0;
  private Process process;
  private String stdout;
  private String stderr;
  private long runtime;

  public ShellFileCommand(final String command, final InputStream stdin)
  {
    try {
      File stdout_file = File.createTempFile("ceva", ".stdout");
      File stderr_file = File.createTempFile("ceva", ".stderr");

      final Vector<String> full_command = ShellStreamCommand
              .getOSPrefix(System.getProperty("os.name"));
      full_command.add(command + " >" + stdout_file.getAbsolutePath() + " 2>"
              + stderr_file.getAbsolutePath());
      LOGGER.debug("running command: {}", full_command);

      final String[] c = full_command.toArray(new String[full_command.size()]);

      runtime = -System.currentTimeMillis();
      process = Runtime.getRuntime().exec(c);
      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(stdin, process.getOutputStream());
      stdin_stream.close();

      process.waitFor();
      runtime += System.currentTimeMillis();

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

  public final InputStream getStdout ()
  {
    return StreamSupport.createInputStream(getStdoutString());
  }

  public final String getStdoutString ()
  {
    if (stdout != null) {
      return stdout;
    }

    return "";
  }

  public final InputStream getStderr ()
  {
    return StreamSupport.createInputStream(getStderrString());
  }

  public final String getStderrString ()
  {
    if (stderr != null) {
      return stderr;
    }

    return "";
  }

  public final int getExitCode ()
  {
    if (process != null) {
      return process.exitValue();
    }

    return -1;
  }

  public final double getRuntime ()
  {
    return runtime / MICROSECONDS_PER_SECOND;
  }
}
