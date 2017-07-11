package de.felixbrandt.ceva.controller;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Helper class to mock Controller objects for unit tests.
 */
public class MockController implements Controller
{
  private Executable last_run_executable;
  private DataSource last_run_source;
  private Object result;
  private int count;

  public MockController()
  {
    this.count = 0;
  }

  public MockController(final Object fixture_result)
  {
    result = fixture_result;
    this.count = 0;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    last_run_executable = executable;
    last_run_source = source;
    this.count++;

    return result;
  }

  public final Object getResult ()
  {
    return result;
  }

  public final int getCallCount ()
  {
    return count;
  }

  public final Executable getLastRunExecutable ()
  {
    return last_run_executable;
  }

  public final DataSource getLastSource ()
  {
    return last_run_source;
  }
}
