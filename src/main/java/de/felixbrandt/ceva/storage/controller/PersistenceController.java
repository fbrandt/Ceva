package de.felixbrandt.ceva.storage.controller;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Decorator sending the wrapped controllers result to a Storage.
 */
public class PersistenceController implements Controller
{
  private final Controller sub_controller;
  private final Storage storage;

  public PersistenceController(final Controller controller, final Storage entity_storage)
  {
    sub_controller = controller;
    storage = entity_storage;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    final Object result = sub_controller.run(executable, source);
    storage.add(result);
    return result;
  }

}
