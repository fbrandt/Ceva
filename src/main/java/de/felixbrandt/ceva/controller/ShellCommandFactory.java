package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ShellFileCommand;

/**
 * Factory creating ShellCommand objects.
 */
public class ShellCommandFactory extends CommandFactory
{
  @Override
  public final Command create (final String command, final InputStream stdin)
  {
    return new ShellFileCommand(command, stdin);
  }
}
