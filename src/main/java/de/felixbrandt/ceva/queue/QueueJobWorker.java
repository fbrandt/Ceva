package de.felixbrandt.ceva.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Controller;

/**
 * Read jobs from queue and execute via controller.
 */
public class QueueJobWorker implements Worker
{
  private static final Logger LOGGER = LogManager.getLogger();

  private static final int WAIT_TIME = 1000;
  private QueueReader<Job> queue;
  private Controller controller;
  private boolean stop_to_work;

  public QueueJobWorker(final QueueReader<Job> _queue, final Controller _controller)
  {
    queue = _queue;
    controller = _controller;
    stop_to_work = false;
  }

  public final void run ()
  {
    LOGGER.debug("start main event loop");

    while (!stop_to_work) {
      final Job job = queue.getNext();
      if (job == null) {
        LOGGER.debug("no job found, waiting for {} ms", WAIT_TIME);
        sleepFor(WAIT_TIME);
      } else {
        LOGGER.debug("running job");
        runJob(job);
      }
    }

    LOGGER.debug("leaving main event loop");
  }

  public final void runJob (final Job job)
  {
    controller.run(job.getExecutable(), job.getSource());
  }

  public final void stop ()
  {
    LOGGER.debug("signal stop of main event loop");
    stop_to_work = true;
  }

  private void sleepFor (final int millis)
  {
    try {
      Thread.sleep(millis);
    } catch (final InterruptedException e) {
      LOGGER.warn("interrupted waiting");
    }
  }
}
