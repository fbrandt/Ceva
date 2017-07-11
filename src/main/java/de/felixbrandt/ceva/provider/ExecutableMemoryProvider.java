package de.felixbrandt.ceva.provider;

import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Simple executable collection, manually set up in memory.
 */
public class ExecutableMemoryProvider implements ExecutableProvider
{
  private final Collection<Executable> collection;

  public ExecutableMemoryProvider()
  {
    collection = new Vector<Executable>();
  }

  public final void add (final Executable executable)
  {
    collection.add(executable);
  }

  public final Collection<Executable> getExecutables ()
  {
    return collection;
  }

}
