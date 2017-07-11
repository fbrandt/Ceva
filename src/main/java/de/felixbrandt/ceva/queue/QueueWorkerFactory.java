package de.felixbrandt.ceva.queue;

import de.felixbrandt.ceva.controller.CommandController;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.VersionProvider;

/**
 * Create QueueJobWorker instances.
 */
public class QueueWorkerFactory implements WorkerFactory
{
  private QueueReader<Job> job_queue;
  private QueueWriter<Object> result_queue;

  public QueueWorkerFactory(final QueueReader<Job> jobs, final QueueWriter<Object> results)
  {
    job_queue = jobs;
    result_queue = results;
  }

  public final Controller setupController (final QueueWriter<Object> queue)
  {
    final CommandFactory command_factory = new ShellCommandFactory();
    final VersionProvider version_provider = new RunVersionProvider(command_factory);

    final Controller command_controller = new CommandController(command_factory,
            version_provider);
    final Controller save_controller = new QueueResultController(command_controller, queue);

    return save_controller;
  }

  public final Worker create ()
  {
    return new QueueJobWorker(job_queue, setupController(result_queue));
  }

}
