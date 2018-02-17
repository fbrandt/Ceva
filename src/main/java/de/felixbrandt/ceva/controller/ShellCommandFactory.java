package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;

/**
 * Factory creating ShellCommand objects.
 */
public class ShellCommandFactory extends CommandFactory
{
  @Override
  public final Command create (final String command, final InputStream stdin,
          final int timelimit)
  {
    return new CevaShellFileCommand(command, stdin, timelimit);
  }
}
