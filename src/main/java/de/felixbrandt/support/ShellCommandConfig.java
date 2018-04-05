package de.felixbrandt.support;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates all parameters of ShellCommand
 */
public class ShellCommandConfig
{
  private String command;
  private InputStream stdin;
  private int timelimit;
  private Map<String, String> environment;

  public ShellCommandConfig()
  {
    this("", StreamSupport.createEmptyInputStream());
  }

  public ShellCommandConfig(final String command, final InputStream stdin)
  {
    this(command, stdin, 0, new HashMap<String, String>());
  }

  public ShellCommandConfig(final String _command, final InputStream _stdin,
          final int _timelimit, final Map<String, String> _environment)
  {
    command = _command;
    stdin = _stdin;
    timelimit = _timelimit;
    environment = _environment;
  }

  public final String getCommand ()
  {
    return command;
  }

  public final void setCommand (final String command)
  {
    this.command = command;
  }

  public final InputStream getStdin ()
  {
    return stdin;
  }

  public final void setStdin (final InputStream stdin)
  {
    this.stdin = stdin;
  }

  public final int getTimelimit ()
  {
    return timelimit;
  }

  public final void setTimelimit (final int timelimit)
  {
    this.timelimit = timelimit;
  }

  public final Map<String, String> getEnvironment ()
  {
    return environment;
  }

  public final void setEnvironment (
          final Map<String, String> additional_environment)
  {
    this.environment = additional_environment;
  }

}
