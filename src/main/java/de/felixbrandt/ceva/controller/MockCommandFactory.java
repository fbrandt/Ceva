package de.felixbrandt.ceva.controller;

import java.io.InputStream;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.StreamSupport;

/**
 * Helper class to mock CommandFactory objects for unit tests.
 */
public class MockCommandFactory extends CommandFactory
{
  private Command fixed_command;
  private String last_command_string;
  private String last_input_stream;

  public MockCommandFactory(final Command _command)
  {
    fixed_command = _command;
  }

  @Override
  public final Command create (final String command, final InputStream stdin)
  {
    last_command_string = command;
    last_input_stream = StreamSupport.getStringFromInputStream(stdin);

    return fixed_command;
  }

  public final String getLastCommandString ()
  {
    return last_command_string;
  }

  public final String getLastInput ()
  {
    return last_input_stream;
  }
}
