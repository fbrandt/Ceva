package de.felixbrandt.ceva.config;

/**
 * A problem instance file.
 */
public interface InstanceFile
{
  boolean isReadable ();

  String getFilename ();

  String getContent ();

  String getHash ();
}
