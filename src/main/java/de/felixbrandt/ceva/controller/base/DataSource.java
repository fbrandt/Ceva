package de.felixbrandt.ceva.controller.base;

import java.io.InputStream;

/**
 * Generic data source for Command execution.
 */
public abstract class DataSource
{
  public abstract String getName ();

  public abstract InputStream getContent (ContentMode mode);

  public abstract Object getObject ();

  public abstract Integer getID ();

  public final InputStream getContent ()
  {
    return getContent(ContentMode.DEFAULT);
  }

  public int getTimelimit ()
  {
    return 0;
  }

  public void doneForNow ()
  {
  }
}
