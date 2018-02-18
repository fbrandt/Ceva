package de.felixbrandt.ceva;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.entity.RuleAware;
import de.felixbrandt.ceva.provider.UnsolvedProvider;

/**
 * Filter instances whose results for the given executable are already present.
 */
public class UnsolvedSourcesFilter implements DataSourceFilter
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final UnsolvedProvider unsolved_provider;
  private final VersionProvider version_provider;

  public UnsolvedSourcesFilter(final UnsolvedProvider storage,
          final VersionProvider provider)
  {
    unsolved_provider = storage;
    version_provider = provider;
  }

  public final Collection<? extends DataSource> doFilter (
          final Executable executable,
          final Collection<? extends DataSource> all_sources)
  {
    final int version = version_provider.getVersion(executable);

    if (executable instanceof RuleAware) {
      return doFilter(executable, version, all_sources);
    }

    return all_sources;
  }

  public final Collection<? extends DataSource> doFilter (
          final Executable executable, final int version,
          final Collection<? extends DataSource> all_sources)
  {
    final HashSet<Integer> unsolved_ids = unsolved_provider
            .getUnsolved(executable, version);
    final List<DataSource> unsolved_sources = new ArrayList<DataSource>();

    for (final DataSource source : all_sources) {
      if (unsolved_ids.contains(source.getID())) {
        unsolved_sources.add(source);
      }
    }

    LOGGER.info("found {} missing values for {}", unsolved_sources.size(),
            executable);

    return unsolved_sources;
  }
}
