package de.felixbrandt.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.charset.Charset;

/**
 * Helper methods for MD5 hashing.
 */
public class HashSupport
{

  private static Logger logger = LogManager.getLogger();
  private static final int LAST_BYTE = 0xFF;

  /** Class cannot be instantiated */
  protected HashSupport()
  {
  }

  public static String doHash (final String string)
  {
    MessageDigest algo;
    try {
      algo = MessageDigest.getInstance("MD5");

      return toHex(algo.digest(string.getBytes(Charset.forName("UTF-8"))));
    } catch (final NoSuchAlgorithmException e) {
      logger.error(e.getMessage());
    }

    return "";
  }

  public static String toHex (final byte[] bytes)
  {
    final StringBuffer result = new StringBuffer();

    for (int i = 0; i < bytes.length; ++i) {
      final String hex = Integer.toHexString(LAST_BYTE & bytes[i]);
      if (hex.length() == 1) {
        result.append('0');
      }
      result.append(hex);
    }

    return result.toString();
  }
}
