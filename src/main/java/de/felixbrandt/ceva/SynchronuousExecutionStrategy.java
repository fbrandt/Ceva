package de.felixbrandt.ceva;

import de.felixbrandt.ceva.controller.CommandController;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.storage.base.Storage;
import de.felixbrandt.ceva.storage.controller.PersistenceController;
import de.felixbrandt.ceva.storage.controller.ToDoController;

/**
 * Synchronuous execution.
 */
public class SynchronuousExecutionStrategy implements ExecutionStrategy
{
  public final Controller setupJobControllerStack (final Storage storage)
  {
    final CommandFactory command_factory = new ShellCommandFactory();
    final VersionProvider version_provider = new RunVersionProvider(command_factory);

    final Controller command_controller = new CommandController(command_factory,
            version_provider);
    final Controller save_controller = new PersistenceController(command_controller, storage);
    final Controller todo_controller = new ToDoController(save_controller, storage,
            version_provider);

    return todo_controller;
  }

  public final void run (final ExecutableProvider executables,
          final DataSourceProvider sources, final Storage storage,
          final DataSourceFilter filter)
  {
    final Controller controller = setupJobControllerStack(storage);
    final BatchControllerService service = new BatchControllerService(executables, sources,
            controller, filter);
    service.run();
  }
}
