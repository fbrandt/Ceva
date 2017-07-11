package de.felixbrandt.ceva.controller.base;

/**
 * Interface to extract a result Object from a CommandResult.
 */
public interface ResultFactory
{
  Object create (Command process);

  void setSource (DataSource source);

  void setVersion (int version);
}
