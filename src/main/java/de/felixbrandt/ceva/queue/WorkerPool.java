package de.felixbrandt.ceva.queue;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manage a pool of CEVA Worker instances.
 */
public class WorkerPool
{
  private static final Logger LOGGER = LogManager.getLogger();
  private Vector<Thread> threads;
  private Vector<Worker> workers;
  private WorkerFactory factory;
  private int count;
  private boolean running;

  public WorkerPool(final WorkerFactory _factory, final int _count)
  {
    threads = new Vector<Thread>();
    workers = new Vector<Worker>();
    factory = _factory;
    count = _count;
  }

  public final void start ()
  {
    if (!running) {
      running = true;

      for (int i = 0; i < count; ++i) {
        final Worker worker = factory.create();
        final Thread thread = new Thread(worker, "worker " + i);
        workers.add(worker);
        threads.add(thread);
        thread.start();
      }
    }
  }

  public final void stop ()
  {
    if (running) {
      running = false;

      for (final Worker worker : workers) {
        worker.stop();
      }
      workers.clear();

      for (final Thread thread : threads) {
        try {
          thread.join();
        } catch (final InterruptedException e) {
          LOGGER.warn(e.getMessage());
        }
      }
      threads.clear();
    }
  }

  public final int activeCount ()
  {
    return threads.size();
  }

  private final void cleanupThreads ()
  {
    for (int i = 0; i < threads.size(); i++) {
      if (threads.get(i).getState() == Thread.State.TERMINATED) {
        try {
          threads.get(i).join();
          workers.remove(i);
          threads.remove(i);
        } catch (InterruptedException e) {
          // do nothing
        }
      }
    }
  }

  public final boolean isRunning ()
  {
    cleanupThreads();

    return running && activeCount() > 0;
  }

}
