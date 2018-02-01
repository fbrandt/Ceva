package de.felixbrandt.ceva.gearman;

import java.util.concurrent.ExecutorService;

import org.gearman.common.GearmanPacketType;
import org.gearman.common.GearmanSessionEvent;
import org.gearman.worker.GearmanWorkerImpl;

/**
 * Extend GearmanWorkerImpl by an idle timeout. If given a timeout > 0 the worker stops, when
 * it did not receive a new job within the time limit (in seconds).
 */
public class GearmanWorkerWithTimeout extends GearmanWorkerImpl
{
  private long last_job_started;
  private long timeout;

  public GearmanWorkerWithTimeout(long timeout_seconds)
  {
    super();
    init(timeout_seconds);
  }

  public GearmanWorkerWithTimeout(int timeout_seconds, ExecutorService executorService)
  {
    super(executorService);
    init(timeout_seconds);
  }

  final private void init (long timeout_seconds)
  {
    last_job_started = System.currentTimeMillis();
    timeout = timeout_seconds * 1000;
  }

  public void handleSessionEvent (GearmanSessionEvent event)
          throws IllegalArgumentException, IllegalStateException
  {
    GearmanPacketType t = event.getPacket().getPacketType();
    if (t == GearmanPacketType.JOB_ASSIGN || t == GearmanPacketType.JOB_ASSIGN_UNIQ) {
      last_job_started = System.currentTimeMillis();
    }

    super.handleSessionEvent(event);
  }

  public boolean isRunning ()
  {
    if (!super.isRunning()) {
      return false;
    }

    if (timeout > 0 && last_job_started + timeout < System.currentTimeMillis()) {
      return false;
    }

    return true;
  }
}
