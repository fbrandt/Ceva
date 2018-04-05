package de.felixbrandt.ceva.controller;

import java.io.InputStream;
import java.util.Map;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ShellCommandConfig;

/**
 * Factory creating ShellCommand objects.
 */
public class ShellCommandFactory extends CommandFactory
{
  @Override
  public final Command create (final String command, final InputStream stdin,
          final int timelimit, final Map<String, String> env)
  {
    return new CevaShellFileCommand(
            new ShellCommandConfig(command, stdin, timelimit, env));
  }
}
