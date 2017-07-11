package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Generic factory to create Command instances.
 */
public abstract class CommandFactory
{
  public abstract Command create (String command, InputStream stdin);

  public final Command create (final String command)
  {
    return create(command, StreamSupport.createEmptyInputStream());
  }
}
