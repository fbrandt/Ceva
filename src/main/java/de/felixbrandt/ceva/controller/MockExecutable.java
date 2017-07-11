package de.felixbrandt.ceva.controller;

import java.util.HashMap;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.support.StringSupport;

/**
 * Helper class to mock Executable objects for unit tests.
 */
public class MockExecutable implements Executable
{
  private String version_path;
  private String run_path;
  private final String name;
  private final MockResultFactory factory;
  private ContentMode mode;
  private int repeat;
  private HashMap<String, String> parameters;
  private boolean invalid_tokens;

  public MockExecutable()
  {
    factory = new MockResultFactory();
    name = "mock executable";
    mode = ContentMode.DEFAULT;
    repeat = 1;
    parameters = new HashMap<String, String>();
  }

  public MockExecutable(final String _version_path, final String _run_path, final String _name)
  {
    version_path = _version_path;
    run_path = _run_path;
    name = _name;
    factory = new MockResultFactory();
    repeat = 1;
    parameters = new HashMap<String, String>();
  }

  public final String getFullVersionPath ()
  {
    return version_path;
  }

  public final String getFullRunPath ()
  {
    if (invalid_tokens) {
      throw new IllegalArgumentException("No parameters were found for some run path tokens");
    }
    return run_path;
  }

  public final String getName ()
  {
    return name;
  }

  public final ResultFactory getResultFactory ()
  {
    return factory;
  }

  public final ContentMode getInputMode ()
  {
    return mode;
  }

  public final int getRepeat ()
  {
    return repeat;
  }

  public final void setRunPath (final String path)
  {
    run_path = path;
  }

  public final void setVersionPath (final String path)
  {
    version_path = path;
  }

  public final void setMode (final ContentMode _mode)
  {
    mode = _mode;
  }

  public final MockResultFactory getMockResultFactory ()
  {
    return factory;
  }

  public final void setRepeat (final int new_repeat)
  {
    repeat = new_repeat;
  }

  public final HashMap<String, String> getParameters ()
  {
    return parameters;
  }

  public final void setParameters (final HashMap<String, String> params)
  {
    parameters = params;
  }

  public final String getParametersAsString ()
  {
    return StringSupport.getMapAsString(parameters);
  }

  public final boolean getInvalidTokens ()
  {
    return invalid_tokens;
  }

  public final void setInvalidTokens (final boolean _invalid_tokens)
  {
    invalid_tokens = _invalid_tokens;
  }
}
