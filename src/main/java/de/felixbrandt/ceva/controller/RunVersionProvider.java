package de.felixbrandt.ceva.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.VersionProvider;

/**
 * Run executable to determine its version.
 */
public class RunVersionProvider implements VersionProvider
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final CommandFactory command_factory;

  public RunVersionProvider(final CommandFactory factory)
  {
    command_factory = factory;
  }

  public final int getVersion (final Executable executable)
  {
    try {
      LOGGER.debug("determining version of {}", executable.getName());
      final String version_path = executable.getFullVersionPath();

      if (version_path == null || version_path.equals("")) {
        LOGGER.debug("no version configured for {}, assuming 0", executable.getName());

        return 0;
      }

      final Command command = command_factory.create(version_path);

      return Integer.parseInt(command.getStdoutString().trim());
    } catch (final NumberFormatException e) {
      LOGGER.error("failed to detect version of {}: {}", executable.getName(), e.getMessage());
    }

    return -1;
  }

}
