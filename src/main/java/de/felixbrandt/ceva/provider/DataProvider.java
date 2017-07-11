package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Rule;

/**
 * Search for Data
 */
public abstract class DataProvider
{
  public final Collection<Data<?, ?>> getData (final Rule rule, final Object source)
  {
    final ArrayList<Object> sources = new ArrayList<Object>();
    sources.add(source);

    return getData(rule, sources);
  }

  public abstract Collection<Data<?, ?>> getData (final Rule rule, final Collection<?> sources);
}
