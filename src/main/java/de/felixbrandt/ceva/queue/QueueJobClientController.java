package de.felixbrandt.ceva.queue;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Send execution request into queue
 */
public class QueueJobClientController implements Controller
{
  private final QueueWriter<Job> job_queue;

  public QueueJobClientController(final QueueWriter<Job> queue)
  {
    job_queue = queue;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    job_queue.add(new Job(executable, source));

    return null;
  }

}
