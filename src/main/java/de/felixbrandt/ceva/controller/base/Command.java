package de.felixbrandt.ceva.controller.base;

import java.io.InputStream;

/**
 * Interface for external (non-java) command execution.
 */
public interface Command
{
  InputStream getStdout ();

  String getStdoutString ();

  InputStream getStderr ();

  String getStderrString ();

  int getExitCode ();

  double getRuntime ();
}
