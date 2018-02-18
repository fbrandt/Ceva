package de.felixbrandt.ceva.gearman;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.worker.AbstractGearmanFunction;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.support.SerializeSupport;

/**
 * Called by Gearman worker to run a job
 */
public class GearmanJobFunction extends AbstractGearmanFunction
{
  private static final Logger LOGGER = LogManager.getLogger();

  private Controller controller;

  public GearmanJobFunction(final String queue_name,
          final Controller _controller)
  {
    super(queue_name);
    controller = _controller;
  }

  @Override
  public final GearmanJobResult executeFunction ()
  {
    final Job job = (Job) SerializeSupport.deserialize((byte[]) data);
    LOGGER.info("running job {} on {}", job.getExecutable(),
            job.getSource().getName());
    final Object result = controller.run(job.getExecutable(), job.getSource());
    final byte[] empty = new byte[0];

    if (result != null) {
      return new GearmanJobResultImpl(jobHandle, true,
              SerializeSupport.serialize(result), empty, empty, 0, 0);
    }

    return new GearmanJobResultImpl(jobHandle, false, empty, empty, empty, 0,
            0);
  }

}
