package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Instance;

/**
 * Provides a preconfigured algorithm.
 */
public class FixedInstanceProvider implements InstanceProvider
{
  private String last_keyword;
  private Collection<Instance> result = new ArrayList<Instance>();

  public FixedInstanceProvider()
  {
    this(new ArrayList<Instance>());
  }

  public FixedInstanceProvider(final Collection<Instance> _result)
  {
    result = _result;
  }

  public final Collection<Instance> findByKeyword (final String keyword)
  {
    last_keyword = keyword;
    return result;
  }

  public final void reset ()
  {
    result.clear();
  }

  public final void add (final Instance instance)
  {
    result.add(instance);
  }

  public final String getLastKeyword ()
  {
    return last_keyword;
  }
}
