package de.felixbrandt.ceva.controller;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.ResultFactory;

/**
 * Helper class to mock ResultFactory objects for unit tests.
 */
public class MockResultFactory implements ResultFactory
{
  private Command last_result;
  private Object return_result;
  private DataSource source;
  private int version;

  public final Object create (final Command process)
  {
    last_result = process;

    return return_result;
  }

  public final void setSource (final DataSource _source)
  {
    source = _source;
  }

  public final void setVersion (final int _version)
  {
    version = _version;
  }

  public final void setReturnResult (final Object result)
  {
    return_result = result;
  }

  public final Command getLastResult ()
  {
    return last_result;
  }

  public final DataSource getSource ()
  {
    return source;
  }

  public final int getVersion ()
  {
    return version;
  }

}
