package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShellStreamCommand extends de.felixbrandt.support.ShellStreamCommand
{
  private static final Logger LOGGER = LogManager.getLogger();

  public ShellStreamCommand(String command, InputStream stdin)
  {
    super(command, stdin);

    try {
      LOGGER.debug("running command: {}", command);
      run();
    } catch (ShellCommandError e) {
      LOGGER.error(e.getMessage());
    }
  }

}
