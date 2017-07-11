package de.felixbrandt.ceva;

import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Strategy how to run the experiments.
 */
public interface ExecutionStrategy
{
  void run (ExecutableProvider executables, DataSourceProvider sources, Storage storage,
          DataSourceFilter filter);
}
