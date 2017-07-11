package de.felixbrandt.ceva.controller;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.VersionProvider;

/**
 * Helper class to mock VersionProvider objects for unit tests.
 */
public class MockVersionProvider implements VersionProvider
{
  private int version;

  public MockVersionProvider(final int _version)
  {
    version = _version;
  }

  public final int getVersion (final Executable executable)
  {
    return version;
  }

}
