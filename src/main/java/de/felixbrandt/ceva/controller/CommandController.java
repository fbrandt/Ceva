package de.felixbrandt.ceva.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.controller.base.VersionProvider;

/**
 * Run Executable as Command (and determine Executable version).
 */
public class CommandController implements Controller
{
  private static final Logger LOGGER = LogManager.getLogger();
  private CommandFactory command_factory;
  private VersionProvider version_provider;

  public CommandController(final CommandFactory factory, final VersionProvider provider)
  {
    command_factory = factory;
    version_provider = provider;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    Object result = null;

    final int version = version_provider.getVersion(executable);

    // executable.getFullRunPath() may throw an exception if there is an undefined
    // token in the run path.
    try {
      final Command command = command_factory.create(executable.getFullRunPath(),
              source.getContent(executable.getInputMode()));

      // only collect result if command did not fail
      if (command.getExitCode() > -1) {
        final ResultFactory factory = executable.getResultFactory();
        factory.setSource(source);
        factory.setVersion(version);
        result = factory.create(command);
      }
    } catch (IllegalArgumentException e) {
      LOGGER.info(e.getMessage());
    }

    return result;
  }

}
