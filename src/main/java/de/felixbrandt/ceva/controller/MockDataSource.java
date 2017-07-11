package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.support.StreamSupport;

/**
 * Helper class to mock DataSource objects for unit tests.
 */
public class MockDataSource extends DataSource
{
  private String filename;
  private InputStream content;
  private ContentMode last_mode;

  public MockDataSource()
  {
    filename = "mock datasource";
    content = StreamSupport.createEmptyInputStream();
  }

  public MockDataSource(final String fixture_filename, final String fixture_content)
  {
    filename = fixture_filename;
    content = StreamSupport.createInputStream(fixture_content);
  }

  @Override
  public final String getName ()
  {
    if (filename == null) {
      return "";
    }

    return filename;
  }

  @Override
  public final InputStream getContent (final ContentMode mode)
  {
    last_mode = mode;

    if (content == null) {
      return StreamSupport.createEmptyInputStream();
    }

    return content;
  }

  public final ContentMode getLastMode ()
  {
    return last_mode;
  }

  @Override
  public final Object getObject ()
  {
    return null;
  }

  @Override
  public final Integer getID ()
  {
    return null;
  }

}
