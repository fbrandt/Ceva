/**
 * @version $Id$
 */
package de.felixbrandt.ceva.metric;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.provider.SolutionProvider;

/**
 * Wrap Solution to act as DataSource.
 */
public class LazySolutionSource extends DataSource implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger();
  private transient SolutionProvider provider;
  private Integer id;
  private SolutionSource source;

  public LazySolutionSource(final SolutionProvider _provider, final Integer _id)
  {
    provider = _provider;
    id = _id;
    source = null;
  }

  private void writeObject (final ObjectOutputStream output) throws IOException
  {
    loadSolution();
    output.defaultWriteObject();
  }

  private void loadSolution ()
  {
    if (source == null && provider != null) {
      source = new SolutionSource(provider.find(id));
    }
  }

  @Override
  public final String getName ()
  {
    loadSolution();
    return source.getName();
  }

  @Override
  public final InputStream getContent (final ContentMode mode)
  {
    loadSolution();
    return source.getContent(mode);
  }

  public static Collection<DataSource> generate (final SolutionProvider provider,
          final Iterable<Integer> ids)
  {
    final Vector<DataSource> result = new Vector<DataSource>();

    for (final Integer id : ids) {
      result.add(new LazySolutionSource(provider, id));
    }

    return result;
  }

  @Override
  public final Object getObject ()
  {
    loadSolution();
    return source.getObject();
  }

  @Override
  public final Integer getID ()
  {
    return id;
  }

  @Override
  public final void doneForNow ()
  {
    if (source != null) {
      // clear cache
      LOGGER.info("clear cache {}", source.getName());
      source = null;
    }
  }
}
