package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;

public class ShellFileCommand extends de.felixbrandt.support.ShellFileCommand
        implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();

  public ShellFileCommand(String command, InputStream stdin)
  {
    super(command, stdin);

    try {
      LOGGER.debug("running command: {}", command);
      run();
    } catch (ShellCommandError e) {
      LOGGER.error(e.getMessage());
    } catch (ShellCommandWarning e) {
      LOGGER.warn(e.getMessage());
    }
  }

}
