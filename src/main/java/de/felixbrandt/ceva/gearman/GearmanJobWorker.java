package de.felixbrandt.ceva.gearman;

import org.gearman.common.GearmanJobServerConnection;
import org.gearman.worker.GearmanFunctionFactory;
import org.gearman.worker.GearmanWorker;

import de.felixbrandt.ceva.queue.Worker;

/**
 * A worker thread performing CEVA jobs.
 */
public final class GearmanJobWorker implements Worker
{
  private GearmanWorker worker;

  public GearmanJobWorker(final GearmanJobServerConnection connection,
          final GearmanFunctionFactory factory, long idle_timeout)
  {
    worker = new GearmanWorkerWithTimeout(idle_timeout);
    worker.addServer(connection);
    worker.registerFunctionFactory(factory);
  }

  public void run ()
  {
    worker.work();
  }

  public void stop ()
  {
    worker.stop();
  }

}
