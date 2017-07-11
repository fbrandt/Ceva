package de.felixbrandt.ceva.controller.base;

/**
 * Determine version of executable.
 */
public interface VersionProvider
{
  int getVersion (final Executable executable);
}
