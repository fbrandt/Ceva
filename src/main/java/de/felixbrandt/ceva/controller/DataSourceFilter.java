package de.felixbrandt.ceva.controller;

import java.util.Collection;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Filter sources for a given executable.
 */
public interface DataSourceFilter
{
  Collection<? extends DataSource> doFilter (final Executable executable,
          final Collection<? extends DataSource> sources);
}
