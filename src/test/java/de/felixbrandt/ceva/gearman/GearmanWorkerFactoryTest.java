package de.felixbrandt.ceva.gearman;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.felixbrandt.ceva.config.QueueConfiguration;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.support.ParameterMap;

public class GearmanWorkerFactoryTest
{

  @Test
  public void testCreateCommandFactory ()
  {
    GearmanWorkerFactory worker_factory = new GearmanWorkerFactory(
            new QueueConfiguration(new ParameterMap()));

    CommandFactory command_factory_1 = worker_factory.createCommandFactory();
    CommandFactory command_factory_2 = worker_factory.createCommandFactory();

    final String osname = System.getProperty("os.name");
    String command_string = "echo %CEVA_WORKER_ID%";
    if (osname.matches("(.*)linux(.*)")) {
      command_string = "echo $CEVA_WORKER_ID";
    }

    Command command_1 = command_factory_1.create(command_string);
    Command command_2 = command_factory_2.create(command_string);

    assertEquals("0", command_1.getStdoutString().trim());
    assertEquals("1", command_2.getStdoutString().trim());
  }
}
