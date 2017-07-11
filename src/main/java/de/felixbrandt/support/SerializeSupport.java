package de.felixbrandt.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper methods for object serialization and deserialization.
 */
public final class SerializeSupport
{
  private static final Logger LOGGER = LogManager.getLogger();

  /** Class cannot be instantiated */
  private SerializeSupport()
  {
  }

  public static byte[] serialize (final Object object)
  {
    final ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
    ObjectOutputStream serializer;
    try {
      serializer = new ObjectOutputStream(out_stream);
      serializer.writeObject(object);
    } catch (final IOException e) {
      LOGGER.error("Could not serialize object of class {}", object.getClass());
      return new byte[0];
    }

    return out_stream.toByteArray();
  }

  public static Object deserialize (final byte[] data)
  {
    final ByteArrayInputStream in_stream = new ByteArrayInputStream(data);
    ObjectInputStream deserializer;
    try {
      deserializer = new ObjectInputStream(in_stream);
      return deserializer.readObject();
    } catch (final IOException e) {
      LOGGER.error("Deserialization failed, IOException" + e.getMessage());
      return null;
    } catch (final ClassNotFoundException e) {
      LOGGER.error("Deserialization failed, object class not found" + e.getMessage());
      return null;
    }
  }
}
