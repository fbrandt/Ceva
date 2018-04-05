package de.felixbrandt.ceva.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ShellCommandConfig;

public class CevaShellFileCommand
        extends de.felixbrandt.support.ShellFileCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();

  public CevaShellFileCommand(final ShellCommandConfig config)
  {
    super(config);

    try {
      LOGGER.debug("running command: {}", config.getCommand());
      run();
    } catch (ShellCommandError e) {
      LOGGER.error(e.getMessage());
    } catch (ShellCommandWarning e) {
      LOGGER.warn(e.getMessage());
    }
  }

}
