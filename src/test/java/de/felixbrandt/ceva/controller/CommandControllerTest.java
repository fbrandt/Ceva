package de.felixbrandt.ceva.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.CommandController;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.MockCommand;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.controller.MockVersionProvider;
import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.support.StreamSupport;

public class CommandControllerTest
{

  class MockCommandFactory extends CommandFactory
  {
    public String last_command;
    public String last_stdin;
    public MockCommand mock_command = new MockCommand("", "", 0);

    @Override
    public Command create (final String command, final InputStream stdin)
    {
      last_command = command;
      last_stdin = StreamSupport.getStringFromInputStream(stdin);

      return mock_command;
    }
  }

  MockCommandFactory command_factory;
  CommandController controller;
  MockExecutable executable;
  MockDataSource source;

  @Before
  public void setup ()
  {
    command_factory = new MockCommandFactory();
    controller = new CommandController(command_factory, new MockVersionProvider(123));
    executable = new MockExecutable("", "RUNPATH", null);
    source = new MockDataSource("filename", "content");
  }

  @Test
  public void testRunCommand ()
  {
    controller.run(executable, source);

    assertEquals(executable.getFullRunPath(), command_factory.last_command);
    assertEquals("content", command_factory.last_stdin);
  }

  @Test
  public void testRunCommandOnStderr ()
  {
    executable.setMode(ContentMode.STDERR);
    controller.run(executable, source);
    assertEquals(ContentMode.STDERR, source.getLastMode());
  }

  @Test
  public void testRunResult ()
  {
    command_factory.mock_command.setStdout("23");
    executable.getMockResultFactory().setReturnResult(new Integer(42));

    final Object result = controller.run(executable, source);

    assertEquals(
            "23",
            StreamSupport.getStringFromInputStream(executable.getMockResultFactory()
                    .getLastResult().getStdout()));
    assertEquals("source in factory set by CommandController", source, executable
            .getMockResultFactory().getSource());
    assertEquals("version in factory set by CommandController", 123, executable
            .getMockResultFactory().getVersion());
    assertTrue(result instanceof Integer);
  }

  @Test
  public void testRunResultFail ()
  {
    command_factory.mock_command.setStdout("STDOUT");
    command_factory.mock_command.setExitCode(-1);
    executable.getMockResultFactory().setReturnResult(new Integer(42));

    final Object result = controller.run(executable, source);

    assertTrue(null == executable.getMockResultFactory().getLastResult());
    assertEquals(null, result);
  }
  
  @Test
  public void testDoNotRunWhenException(){
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("param1", "val1");
    
    executable.setRunPath("cmd1 {param1} | cmd2 {param2}");
    executable.setParameters(params);
    executable.setInvalidTokens(true);
    executable.getMockResultFactory().setReturnResult(new Integer(42));
        
    command_factory.mock_command.setStdout("STDOUT");
    command_factory.mock_command.setExitCode(0);
        
    final Object result = controller.run(executable, source);
    
    assertEquals(null, result);
  }
}
