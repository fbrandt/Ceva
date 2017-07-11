package de.felixbrandt.support;

import java.io.InputStream;

/**
 * Asynchronous reading of an InputStream into a String.
 */
public final class StreamedString extends Thread
{
  private InputStream in;
  private String value;

  public StreamedString(final InputStream input_stream)
  {
    in = input_stream;
    value = "";
  }

  public String getString ()
  {
    return value;
  }

  @Override
  public void run ()
  {
    value = StreamSupport.getStringFromInputStream(in);
  }

  public static StreamedString create (final InputStream in)
  {
    final StreamedString ss = new StreamedString(in);

    // start the thread
    ss.start();

    return ss;
  }
}
