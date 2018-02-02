package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Helper class to mock Command objects for unit tests.
 */
public class MockCommand implements Command
{
  private String stdout;
  private String stderr;
  private int exit_code;
  private double runtime;

  public MockCommand()
  {
    this("", "", 0);
  }

  public MockCommand(final String output, final String errors, final int code)
  {
    stdout = output;
    stderr = errors;
    exit_code = code;
  }

  public final InputStream getStdout ()
  {
    return StreamSupport.createInputStream(stdout);
  }

  public final String getStdoutString ()
  {
    return stdout;
  }

  public final InputStream getStderr ()
  {
    return StreamSupport.createInputStream(stderr);
  }

  public final String getStderrString ()
  {
    return stderr;
  }

  public final int getExitcode ()
  {
    return exit_code;
  }

  public final void setStdout (final String output)
  {
    stdout = output;
  }

  public final void setStderr (final String output)
  {
    stderr = output;
  }

  public final void setExitcode (final int code)
  {
    exit_code = code;
  }

  public final double getRuntime ()
  {
    return runtime;
  }

  public final void setRuntime (final double _runtime)
  {
    runtime = _runtime;
  }
}
