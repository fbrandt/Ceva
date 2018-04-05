package de.felixbrandt.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Generic base class for executing an external process.
 */
public abstract class ShellCommand
{
  private static final double MICROSECONDS_PER_SECOND = 1000.0;
  public static final int TIMELIMIT_EXCEEDED = 124;
  private ShellCommandConfig config;
  private Process process;
  private long process_start_time;
  private long process_finish_time;
  private boolean timelimit_exceeded;

  public static class ShellCommandError extends Exception
  {
    private static final long serialVersionUID = 1L;

    ShellCommandError(String message)
    {
      super(message);
    }
  }

  public static class ShellCommandWarning extends Exception
  {
    private static final long serialVersionUID = 1L;

    ShellCommandWarning(String message)
    {
      super(message);
    }
  }

  public ShellCommand(ShellCommandConfig config)
  {
    this.config = config;
  }

  public final String getCommand ()
  {
    return config.getCommand();
  }

  protected final InputStream getStdin ()
  {
    return config.getStdin();
  }

  protected final Process startProcess (final String[] command)
          throws IOException
  {
    if (process == null) {
      process_start_time = System.currentTimeMillis();
      ProcessBuilder process_builder = new ProcessBuilder(command);
      if (config.getEnvironment() != null) {
        mergeEnvironment(config.getEnvironment(),
                process_builder.environment());
      }
      process = process_builder.start();
    }

    return process;
  }

  public static void mergeEnvironment (final Map<String, String> from,
          final Map<String, String> to)
  {
    if (from != null && to != null) {
      from.forEach( (key, value) -> {
        to.put(key, value);
      });
    }
  }

  protected final void waitForProcess ()
          throws InterruptedException, ShellCommandWarning
  {
    boolean finished = true;
    if (config.getTimelimit() == 0) {
      process.waitFor();
    } else {
      finished = process.waitFor(config.getTimelimit(), TimeUnit.SECONDS);
    }
    process_finish_time = System.currentTimeMillis();

    if (!finished) {
      timelimit_exceeded = true;
      process.destroyForcibly();
      while (process.isAlive()) {
        Thread.sleep(100);
      }
      throw new ShellCommandWarning("timelimit exceeded");
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

  public final int getExitcode ()
  {
    if (timelimit_exceeded) {
      return TIMELIMIT_EXCEEDED;
    }

    if (process != null) {
      return process.exitValue();
    }

    return -1;
  }

  public final double getRuntime ()
  {
    return (process_finish_time - process_start_time) / MICROSECONDS_PER_SECOND;
  }

  public final InputStream getStdout ()
  {
    return StreamSupport.createInputStream(getStdoutString());
  }

  public abstract String getStdoutString ();

  public final InputStream getStderr ()
  {
    return StreamSupport.createInputStream(getStderrString());
  }

  public abstract String getStderrString ();

}
