package de.felixbrandt.ceva;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanClientImpl;
import org.gearman.client.GearmanJobResult;
import org.gearman.common.GearmanNIOJobServerConnection;

import de.felixbrandt.ceva.config.QueueConfiguration;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.gearman.GearmanLoopQueueReader;
import de.felixbrandt.ceva.gearman.GearmanQueueWriter;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Run jobs via external Gearman job server and collect results.
 */
public class GearmanExecutionStrategy implements ExecutionStrategy
{
  private final QueueConfiguration config;
  private final GearmanClient client;
  private static final long GEARMAN_TIMEOUT = 5000;

  public GearmanExecutionStrategy(final QueueConfiguration _config)
  {
    config = _config;
    client = new GearmanClientImpl(GEARMAN_TIMEOUT);
    client.addJobServer(new GearmanNIOJobServerConnection(config.getHost(), config.getPort()));
  }

  public final void run (final ExecutableProvider executables,
          final DataSourceProvider sources, final Storage storage,
          final DataSourceFilter filter)
  {
    final List<Future<GearmanJobResult>> pending_jobs;
    pending_jobs = new ArrayList<Future<GearmanJobResult>>();

    final String queue_name = config.getJobQueueName();
    final QueueWriter<Job> jobs = new GearmanQueueWriter<Job>(client, queue_name,
            pending_jobs);
    final QueueReader<Object> results = new GearmanLoopQueueReader<Object>(pending_jobs);

    final QueueExecutionStrategy strategy = new QueueExecutionStrategy(jobs, results);
    strategy.run(executables, sources, storage, filter);
  }
}
