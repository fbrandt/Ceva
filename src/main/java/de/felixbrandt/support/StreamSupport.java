package de.felixbrandt.support;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper methods for easier handling of Streams.
 */
public final class StreamSupport
{

  private static final Logger LOGGER = LogManager.getLogger();

  public static final int BUFFER_SIZE = 102400;

  /** Class cannot be instantiated */
  private StreamSupport()
  {
  }

  public static String getStringFromInputStream (final InputStream is)
  {

    BufferedReader br = null;
    final StringBuilder sb = new StringBuilder();

    try {
      final char[] buffer = new char[BUFFER_SIZE];
      br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      int len = 0;
      while ((len = br.read(buffer)) > -1) {
        sb.append(buffer, 0, len);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }

    return sb.toString();
  }

  public static void copyStream (final InputStream in, final OutputStream out)
  {
    final byte[] buffer = new byte[BUFFER_SIZE];
    int len = 0;

    try {
      while ((len = in.read(buffer)) != -1) {
        out.write(buffer, 0, len);
      }
    } catch (final Exception e) {
      LOGGER.error("failed to copy stream: " + e.getMessage());
    }
  }

  public static InputStream createEmptyInputStream ()
  {
    return StreamSupport.createInputStream("");
  }

  public static InputStream createInputStream (final String value)
  {
    try {
      return new ByteArrayInputStream(value.getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      LOGGER.error("UnsupportedEncoding when transforming String \"{}\" to Stream", value);
      return new ByteArrayInputStream(new byte[0]);
    }
  }

}
