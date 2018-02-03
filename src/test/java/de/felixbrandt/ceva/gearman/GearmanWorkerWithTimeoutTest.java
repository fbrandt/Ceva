package de.felixbrandt.ceva.gearman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gearman.common.GearmanPacketImpl;
import org.gearman.common.GearmanPacketType;
import org.junit.Test;

public class GearmanWorkerWithTimeoutTest
{
  class FakeGearmanPacket extends GearmanPacketImpl
  {
    boolean first_call = true;

    public FakeGearmanPacket()
    {
      super(null, GearmanPacketType.NOOP, new byte[0]);
    }

    public GearmanPacketType getPacketType ()
    {
      try {
        if (first_call) {
          first_call = false;
          Thread.sleep(2000);
          return GearmanPacketType.NOOP;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      return GearmanPacketType.JOB_ASSIGN;
    }
  }

  @Test
  public void testTimeout () throws InterruptedException
  {
    GearmanWorkerWithTimeout worker = new GearmanWorkerWithTimeout(1);
    assertFalse(worker.reachedTimeout());
    Thread.sleep(2000);
    assertTrue(worker.reachedTimeout());
  }

  @Test
  public void testTimeoutLongRunningJobs () throws InterruptedException
  {
    GearmanWorkerWithTimeout worker = new GearmanWorkerWithTimeout(1);
    worker.updateTimeout(GearmanPacketType.JOB_ASSIGN);
    Thread.sleep(2000);
    // no timeout if we are running a job
    assertFalse(worker.reachedTimeout());
    worker.updateTimeout(GearmanPacketType.NO_JOB);
    Thread.sleep(500);
    // timeout is reset on any packet type
    assertFalse(worker.reachedTimeout());
    Thread.sleep(1000);
    // timeout when no packet was received
    assertTrue(worker.reachedTimeout());
  }
}
