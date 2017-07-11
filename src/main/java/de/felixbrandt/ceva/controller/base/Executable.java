package de.felixbrandt.ceva.controller.base;

import java.util.Map;

/**
 * Interface for executable commands.
 */
public interface Executable
{
  String getFullVersionPath ();

  String getFullRunPath ();

  String getName ();

  ResultFactory getResultFactory ();

  ContentMode getInputMode ();

  int getRepeat ();

  Map<String, String> getParameters ();

  String getParametersAsString ();
}
