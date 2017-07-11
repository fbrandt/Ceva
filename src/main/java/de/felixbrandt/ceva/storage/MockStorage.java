package de.felixbrandt.ceva.storage;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Helper class to mock Storage objects for unit tests.
 */
public class MockStorage implements Storage
{
  private boolean exists;
  private Object last_added;
  private int add_count;
  private int count;

  public MockStorage()
  {
    exists = false;
    add_count = 0;
  }

  public final boolean exists (final Executable executable, final DataSource source,
          final int version)
  {
    return exists;
  }

  public final void add (final Object data)
  {
    last_added = data;
    add_count++;
  }

  public final int getAddCount ()
  {
    return add_count;
  }

  public final void setExists (final boolean _exists)
  {
    exists = _exists;
  }

  public final Object getLastAdded ()
  {
    return last_added;
  }

  public final void setCount (final int _count)
  {
    count = _count;
  }

  public final int count (final Executable executable, final DataSource source,
          final int version)
  {
    return count;
  }
}
