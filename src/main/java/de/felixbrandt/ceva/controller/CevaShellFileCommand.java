package de.felixbrandt.ceva.controller;

import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;

public class CevaShellFileCommand
        extends de.felixbrandt.support.ShellFileCommand implements Command
{
  private static final Logger LOGGER = LogManager.getLogger();

  public CevaShellFileCommand(String command, InputStream stdin)
  {
    this(command, stdin, 0, null);
  }

  public CevaShellFileCommand(final String command, final InputStream stdin,
          final int timelimit, final Map<String, String> env)
  {
    super(command, stdin, timelimit, env);

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
