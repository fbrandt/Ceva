package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.felixbrandt.ceva.SynchronuousExecutionStrategy;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.provider.DataSourceMemoryProvider;
import de.felixbrandt.ceva.provider.ExecutableMemoryProvider;
import de.felixbrandt.ceva.storage.MockStorage;

public class SynchronuousExecutionStrategyTest
{
  @Test
  public void testDoRun ()
  {
    final SynchronuousExecutionStrategy strategy = new SynchronuousExecutionStrategy();
    final MockStorage storage = new MockStorage();
    final ExecutableMemoryProvider executables = new ExecutableMemoryProvider();
    final MockExecutable executable = new MockExecutable();
    executable.setRunPath("echo 1");

    executables.add(executable);
    executables.add(executable);

    final DataSourceMemoryProvider sources = new DataSourceMemoryProvider();
    sources.add(new MockDataSource());
    sources.add(new MockDataSource());

    strategy.run(executables, sources, storage, null);
    assertEquals(4, storage.getAddCount());
  }

}
