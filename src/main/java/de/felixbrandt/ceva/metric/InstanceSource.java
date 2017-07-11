package de.felixbrandt.ceva.metric;

import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.support.StreamSupport;

/**
 * Wrap Instance to act as DataSource.
 */
public class InstanceSource extends DataSource implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private Instance instance;

  public InstanceSource(final Instance _instance)
  {
    instance = _instance;
  }

  public final Instance getInstance ()
  {
    return instance;
  }

  @Override
  public final String getName ()
  {
    return instance.getName();
  }

  @Override
  public final InputStream getContent (final ContentMode mode)
  {
    return StreamSupport.createInputStream(instance.getContent());
  }

  public static Collection<DataSource> generate (final Iterable<Instance> instances)
  {
    final Vector<DataSource> result = new Vector<DataSource>();

    for (final Instance instance : instances) {
      result.add(new InstanceSource(instance));
    }

    return result;
  }

  @Override
  public final Object getObject ()
  {
    return instance;
  }

  @Override
  public final Integer getID ()
  {
    return instance.getInstance();
  }
}
