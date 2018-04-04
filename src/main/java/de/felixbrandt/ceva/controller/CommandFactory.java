package de.felixbrandt.ceva.controller;

import java.io.InputStream;
import java.util.Map;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Generic factory to create Command instances.
 */
public abstract class CommandFactory
{
  public abstract Command create (String command, InputStream stdin,
          int timelimit, Map<String, String> env);

  public final Command create (final String command, final InputStream stdin,
          final int timelimit)
  {
    return create(command, stdin, timelimit, null);
  }

  public final Command create (String command, InputStream stdin)
  {
    return create(command, stdin, 0);
  }

  public final Command create (final String command)
  {
    return create(command, StreamSupport.createEmptyInputStream());
  }
}
