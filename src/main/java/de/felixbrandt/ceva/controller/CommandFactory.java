package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Generic factory to create Command instances.
 */
public abstract class CommandFactory
{
  public abstract Command create (String command, InputStream stdin, int timelimit);

  public final Command create (String command, InputStream stdin)
  {
    return create(command, stdin, 0);
  }

  public final Command create (final String command)
  {
    return create(command, StreamSupport.createEmptyInputStream());
  }
}
