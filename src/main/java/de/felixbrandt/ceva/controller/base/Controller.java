package de.felixbrandt.ceva.controller.base;

/**
 * Interface to run Executables.
 */
public interface Controller
{
  Object run (Executable executable, DataSource source);
}
