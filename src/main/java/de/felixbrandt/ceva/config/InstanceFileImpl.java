package de.felixbrandt.ceva.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.support.HashSupport;
import de.felixbrandt.support.StreamSupport;

/**
 * A detected problem instance.
 */
public class InstanceFileImpl implements InstanceFile
{
  private static final Logger LOGGER = LogManager.getLogger();
  private File file;

  public InstanceFileImpl(final String path)
  {
    file = new File(path);
  }

  public final boolean isReadable ()
  {
    return file.canRead();
  }

  public final String getFilename ()
  {
    return file.getName();
  }

  public final String getContent ()
  {
    try {
      return StreamSupport.getStringFromInputStream(new FileInputStream(file.getPath()));
    } catch (final FileNotFoundException e) {
      LOGGER.error("could not read file: " + file.toString());
      LOGGER.error(e.getMessage());
    }

    return "";
  }

  public final String getHash ()
  {
    return HashSupport.doHash(getContent());
  }
}
