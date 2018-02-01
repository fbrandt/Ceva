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
  private Process process;
  private long process_started;
  private long process_ended;

  protected final Process startProcess (final String[] command) throws IOException
  {
    if (process == null) {
      process_started = System.currentTimeMillis();
      process = Runtime.getRuntime().exec(command);
    }

    return process;
  }

  protected final void waitForProcess () throws InterruptedException
  {
    process.waitFor();
    process_ended = System.currentTimeMillis();
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
    return (process_ended - process_started) / MICROSECONDS_PER_SECOND;
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
