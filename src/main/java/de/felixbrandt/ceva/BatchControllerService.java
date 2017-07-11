package de.felixbrandt.ceva;

import java.util.Collection;

import de.felixbrandt.ceva.controller.BatchController;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;

/**
 * Batch processing of all executable-source-pairs using the given controller.
 */
public final class BatchControllerService implements Runnable
{
  private final ExecutableProvider executable_provider;
  private final DataSourceProvider source_provider;
  private final Controller controller_stack;
  private final DataSourceFilter source_filter;

  public BatchControllerService(final ExecutableProvider executables,
          final DataSourceProvider sources, final Controller controller)
  {
    this(executables, sources, controller, null);
  }

  public BatchControllerService(final ExecutableProvider executables,
          final DataSourceProvider sources, final Controller controller,
          final DataSourceFilter filter)
  {
    executable_provider = executables;
    source_provider = sources;
    controller_stack = controller;
    source_filter = filter;
  }

  public void run ()
  {
    final Collection<? extends Executable> metrics = executable_provider.getExecutables();
    final Collection<? extends DataSource> instances = source_provider.getDataSources();

    new BatchController(controller_stack, source_filter).run(metrics, instances);
  }

}
