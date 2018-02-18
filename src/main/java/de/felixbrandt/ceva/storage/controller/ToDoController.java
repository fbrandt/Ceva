package de.felixbrandt.ceva.storage.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Decorator only calling the Controller for missing results.
 */
public class ToDoController implements Controller
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final Controller controller;
  private final Storage storage;
  private final VersionProvider version_provider;

  public ToDoController(final Controller sub_controller,
          final Storage done_storage, final VersionProvider provider)
  {
    controller = sub_controller;
    storage = done_storage;
    version_provider = provider;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    boolean solution_exists = storage.exists(executable, source,
            version_provider.getVersion(executable));
    int todo_repeat = executable.getRepeat() - storage.count(executable, source,
            version_provider.getVersion(executable));
    boolean solution_pending = todo_repeat > 0;

    if (!solution_exists || solution_pending) {
      return runAll(executable, source, todo_repeat);
    } else {
      LOGGER.debug("skipping {} for {} because result is already present",
              executable, source.getName());
    }

    return null;
  }

  public final Object runAll (final Executable executable,
          final DataSource source, final int todo_repeat)
  {
    Object last_solution = null;

    for (int i = 0; i < todo_repeat; i++) {
      last_solution = controller.run(executable, source);
    }

    return last_solution;
  }
}
