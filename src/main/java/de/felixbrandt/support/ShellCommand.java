package de.felixbrandt.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Vector;

/**
 * Generic base class for executing an external process.
 */
public abstract class ShellCommand
{
  private static final double MICROSECONDS_PER_SECOND = 1000.0;
  private String command;
  private InputStream stdin;
  private Process process;
  private long process_start_time;
  private long process_finish_time;

  public class ShellCommandError extends Exception
  {
    private static final long serialVersionUID = 1L;

    ShellCommandError(String message)
    {
      super(message);
    }
  }

  public class ShellCommandWarning extends Exception
  {
    private static final long serialVersionUID = 1L;

    ShellCommandWarning(String message)
    {
      super(message);
    }
  }

  public ShellCommand(String _command, InputStream _stdin)
  {
    command = _command;
    stdin = _stdin;
  }

  public final String getCommand ()
  {
    return command;
  }

  protected final InputStream getStdin ()
  {
    return stdin;
  }

  protected final Process startProcess (final String[] command) throws IOException
  {
    if (process == null) {
      process_start_time = System.currentTimeMillis();
      process = Runtime.getRuntime().exec(command);
    }

    return process;
  }

  protected final void waitForProcess () throws InterruptedException
  {
    process.waitFor();
    process_finish_time = System.currentTimeMillis();
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

  public final int getExitCode ()
  {
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
