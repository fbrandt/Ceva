package de.felixbrandt.ceva.controller;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Execute wrapped controller for each combination of Executable and DataSource.
 */
public class BatchController
{
  private static final Logger LOGGER = LogManager.getLogger();
  private Controller wrapped_controller;
  private DataSourceFilter source_filter;

  public BatchController(final Controller controller)
  {
    wrapped_controller = controller;
    source_filter = null;
  }

  public BatchController(final Controller controller, final DataSourceFilter filter)
  {
    wrapped_controller = controller;
    source_filter = filter;
  }

  public final void run (final Iterable<? extends Executable> executables,
          final Collection<? extends DataSource> sources)
  {
    for (final Executable executable : executables) {
      runFiltered(executable, sources);
    }
  }

  public final void runFiltered (final Executable executable,
          final Collection<? extends DataSource> sources)
  {
    if (source_filter != null) {
      LOGGER.info("filter sources for {}", executable.getName());
      run(executable, source_filter.doFilter(executable, sources));
    } else {
      run(executable, sources);
    }
  }

  public final void run (final Executable executable,
          final Collection<? extends DataSource> sources)
  {
    int i = 0;
    final int n = sources.size();

    for (final DataSource source : sources) {
      LOGGER.info("running {} for {}({}/{})", executable.getName(), source.getName(), i++, n);
      run(executable, source);
      source.doneForNow();
    }
  }

  public final void run (final Iterable<? extends Executable> executables,
          final DataSource source)
  {
    for (final Executable executable : executables) {
      LOGGER.info("running {} for {}", executable.getName(), source.getName());
      run(executable, source);
    }
    source.doneForNow();
  }

  public final void run (final Executable executable, final DataSource source)
  {
    wrapped_controller.run(executable, source);
  }

  public final void setFilter (final DataSourceFilter filter)
  {
    source_filter = filter;
  }
}
