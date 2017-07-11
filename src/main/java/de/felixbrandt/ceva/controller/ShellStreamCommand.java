package de.felixbrandt.ceva.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;
import de.felixbrandt.support.StreamedString;

/**
 * [DEPRECATED] Command execution in a windows command shell using the input/stdout/stderr
 * streams.
 *
 * This is deprecated, because Java blocks the external process if the output streams cannot be
 * processed fast enough. On machines running 4 workers, the runtimes of the workers was
 * sometimes doubled, compared to runs outside CEVA.
 */
public class ShellStreamCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();
  private static final double MICROSECONDS_PER_SECOND = 1000.0;
  private Process process;
  private StreamedString stdout;
  private StreamedString stderr;
  private long runtime;

  public ShellStreamCommand(final String command, final InputStream stdin)
  {
    try {
      final Vector<String> full_command = getOSPrefix(System.getProperty("os.name"));
      full_command.add(command);
      LOGGER.debug("running command: {}", full_command);

      final String[] c = full_command.toArray(new String[full_command.size()]);

      runtime = -System.currentTimeMillis();
      process = Runtime.getRuntime().exec(c);
      final OutputStream stdin_stream = process.getOutputStream();
      StreamSupport.copyStream(stdin, process.getOutputStream());
      stdin_stream.close();

      stdout = StreamedString.create(process.getInputStream());
      stderr = StreamedString.create(process.getErrorStream());

      process.waitFor();
      runtime += System.currentTimeMillis();

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

  public static Vector<String> getOSPrefix (final String _osname)
  {
    final String osname = _osname.toLowerCase(Locale.ENGLISH);
    final Vector<String> result = new Vector<String>();

    if (osname.matches("(.*)windows(.*)")) {
      result.add("cmd");
      result.add("/c");
    } else if (osname.matches("(.*)linux(.*)")) {
      result.add("/bin/bash");
      result.add("-c");
    }

    return result;
  }

  public final InputStream getStdout ()
  {
    return StreamSupport.createInputStream(getStdoutString());
  }

  public final String getStdoutString ()
  {
    if (stdout != null) {
      return stdout.getString();
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
      return stderr.getString();
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
