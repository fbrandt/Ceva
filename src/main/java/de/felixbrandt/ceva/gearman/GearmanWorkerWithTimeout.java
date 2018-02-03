package de.felixbrandt.ceva.gearman;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gearman.common.GearmanPacketType;
import org.gearman.common.GearmanSessionEvent;
import org.gearman.worker.GearmanWorkerImpl;

/**
 * Extend GearmanWorkerImpl by an idle timeout. If given a timeout > 0 the worker stops, when
 * it did not receive a new job within the time limit (in seconds).
 */
public class GearmanWorkerWithTimeout extends GearmanWorkerImpl
{
  private static final Logger LOGGER = LogManager.getLogger();
  private long last_packet;
  private boolean has_job = false;
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
    last_packet = System.currentTimeMillis();
    timeout = timeout_seconds * 1000;
  }

  public void handleSessionEvent (GearmanSessionEvent event)
          throws IllegalArgumentException, IllegalStateException
  {
    super.handleSessionEvent(event);
    updateTimeout(event.getPacket().getPacketType());
  }

  public void updateTimeout (GearmanPacketType type)
  {
    LOGGER.debug("handle Packet of Type {}", type);
    has_job = type == GearmanPacketType.JOB_ASSIGN
            || type == GearmanPacketType.JOB_ASSIGN_UNIQ;
    last_packet = System.currentTimeMillis();
  }

  public boolean isRunning ()
  {
    return super.isRunning() && !reachedTimeout();
  }

  public boolean reachedTimeout ()
  {
    return timeout > 0 && !has_job && last_packet + timeout < System.currentTimeMillis();
  }
}
