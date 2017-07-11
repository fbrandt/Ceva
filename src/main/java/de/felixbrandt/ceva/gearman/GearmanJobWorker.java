package de.felixbrandt.ceva.gearman;

import org.gearman.common.GearmanJobServerConnection;
import org.gearman.worker.GearmanFunctionFactory;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;

import de.felixbrandt.ceva.queue.Worker;

/**
 * A worker thread performing CEVA jobs.
 */
public final class GearmanJobWorker implements Worker
{
  private GearmanWorker worker;

  public GearmanJobWorker(final GearmanJobServerConnection connection,
          final GearmanFunctionFactory factory)
  {
    worker = new GearmanWorkerImpl();
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
