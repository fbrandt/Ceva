package de.felixbrandt.ceva.provider;

import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.DataSource;

/**
 * Simple datasource collection, manually set up in memory.
 */
public class DataSourceMemoryProvider implements DataSourceProvider
{
  private final Collection<DataSource> collection;

  public DataSourceMemoryProvider()
  {
    collection = new Vector<DataSource>();
  }

  public final void add (final DataSource source)
  {
    collection.add(source);
  }

  public final Collection<DataSource> getDataSources ()
  {
    return collection;
  }

}
