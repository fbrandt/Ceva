package de.felixbrandt.ceva;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.queue.BaseQueue;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueWorkerFactory;
import de.felixbrandt.ceva.queue.WorkerFactory;
import de.felixbrandt.ceva.queue.WorkerPool;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Start multiple workers in the same jvm to do the work
 */
public class WorkerExecutionStrategy implements ExecutionStrategy
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final int worker_count;

  public WorkerExecutionStrategy(final int _worker_count)
  {
    worker_count = _worker_count;
  }

  public final void run (final ExecutableProvider executables,
          final DataSourceProvider sources, final Storage storage,
          final DataSourceFilter filter)
  {
    final BaseQueue<Job> job_queue = new BaseQueue<Job>(new ConcurrentLinkedQueue<Job>());
    final BaseQueue<Object> result_queue = new BaseQueue<Object>(
            new ConcurrentLinkedQueue<Object>());
    final WorkerFactory factory = new QueueWorkerFactory(job_queue, result_queue);
    final WorkerPool workers = new WorkerPool(factory, worker_count);
    workers.start();

    final QueueExecutionStrategy strategy = new QueueExecutionStrategy(job_queue,
            result_queue);
    LOGGER.debug("run execution strategy");
    strategy.run(executables, sources, storage, filter);

    workers.stop();
  }
}
