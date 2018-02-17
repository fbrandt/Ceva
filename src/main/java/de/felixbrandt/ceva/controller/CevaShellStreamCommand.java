package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CevaShellStreamCommand extends de.felixbrandt.support.ShellStreamCommand
{
  private static final Logger LOGGER = LogManager.getLogger();

  public CevaShellStreamCommand(String command, InputStream stdin)
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
