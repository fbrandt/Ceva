package de.felixbrandt.ceva.storage.base;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Save results of calculations in database.
 */
public interface Storage
{
  boolean exists (Executable executable, DataSource source, int version);

  void add (Object data);

  int count (Executable executable, DataSource source, int version);
}
