package de.felixbrandt.ceva.gearman;

import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanClientImpl;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.worker.GearmanFunctionFactory;

import de.felixbrandt.ceva.config.QueueConfiguration;
import de.felixbrandt.ceva.controller.CommandController;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.queue.Worker;
import de.felixbrandt.ceva.queue.WorkerFactory;

/**
 * Create GearmanJobWorker instances.
 */
public class GearmanWorkerFactory implements WorkerFactory
{
  private QueueConfiguration config;

  public GearmanWorkerFactory(final QueueConfiguration _config)
  {
    config = _config;
  }

  public final Controller getControllerStack ()
  {
    final GearmanClient client = new GearmanClientImpl();
    client.addJobServer(new GearmanNIOJobServerConnection(config.getHost(), config.getPort()));

    final CommandFactory command_factory = new ShellCommandFactory();
    final VersionProvider version_provider = new RunVersionProvider(command_factory);
    final Controller command_controller = new CommandController(command_factory,
            version_provider);

    return command_controller;
  }

  public final GearmanFunctionFactory getFunctionFactory ()
  {
    final Controller controller = getControllerStack();
    return new GearmanJobFunctionFactory(config.getJobQueueName(), controller);
  }

  public final Worker create ()
  {
    final GearmanJobServerConnection conn = new GearmanNIOJobServerConnection(config.getHost(),
            config.getPort());

    return new GearmanJobWorker(conn, getFunctionFactory());
  }

}
