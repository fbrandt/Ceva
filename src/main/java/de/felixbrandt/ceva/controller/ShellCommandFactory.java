package de.felixbrandt.ceva.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ShellCommand;
import de.felixbrandt.support.ShellCommandConfig;

/**
 * Factory creating ShellCommand objects.
 */
public class ShellCommandFactory extends CommandFactory
{
  private Map<String, String> factory_environment;

  public ShellCommandFactory()
  {
    this(new HashMap<String, String>());
  }

  public ShellCommandFactory(final Map<String, String> env)
  {
    super();
    this.factory_environment = env;
  }

  @Override
  public final Command create (final String command, final InputStream stdin,
          final int timelimit, final Map<String, String> command_environment)
  {
    Map<String, String> environment = new HashMap<String, String>(
            factory_environment);
    ShellCommand.mergeEnvironment(command_environment, environment);
    return new CevaShellFileCommand(
            new ShellCommandConfig(command, stdin, timelimit, environment));
  }
}
