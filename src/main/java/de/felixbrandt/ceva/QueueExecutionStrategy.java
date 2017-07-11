package de.felixbrandt.ceva;

import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueJobClientController;
import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueReaderStatus;
import de.felixbrandt.ceva.queue.QueueResultCollector;
import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.ceva.queue.QueueWriterStatus;
import de.felixbrandt.ceva.storage.base.Storage;
import de.felixbrandt.ceva.storage.controller.ToDoController;

/**
 * Send execution jobs to a queue and read their results from another queue.
 */
public final class QueueExecutionStrategy implements ExecutionStrategy
{
  private final QueueWriterStatus<Job> job_queue;
  private final QueueReaderStatus<Object> result_queue;

  public QueueExecutionStrategy(final QueueWriter<Job> _job_queue,
          final QueueReader<Object> results)
  {
    job_queue = new QueueWriterStatus<Job>(_job_queue);
    result_queue = new QueueReaderStatus<Object>(job_queue, results);
  }

  public Controller setupSenderStack (final Storage storage)
  {
    final CommandFactory command_factory = new ShellCommandFactory();
    final VersionProvider version_provider = new RunVersionProvider(command_factory);

    final Controller job_controller = new QueueJobClientController(job_queue);
    final Controller todo_controller = new ToDoController(job_controller, storage,
            version_provider);

    return todo_controller;
  }

  public void run (final ExecutableProvider executables, final DataSourceProvider sources,
          final Storage storage, final DataSourceFilter filter)
  {
    final Controller controller = setupSenderStack(storage);
    final BatchControllerService service = new BatchControllerService(executables, sources,
            controller, filter);

    service.run();
    job_queue.setDone(true);

    final QueueResultCollector collector = new QueueResultCollector(storage, result_queue);
    collector.collect();
  }
}
