package de.felixbrandt.ceva.gearman;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.gearman.client.GearmanIOEventListener;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobResult;

/**
 * Helper class for unit testing.
 */
class MockGearmanJob implements GearmanJob
{
  private GearmanJobResult result;

  public MockGearmanJob(final GearmanJobResult _result)
  {
    result = _result;
  }

  public GearmanJobResult call () throws Exception
  {
    return null;
  }

  public boolean cancel (final boolean may_interupt_if_running)
  {
    return false;
  }

  public boolean isCancelled ()
  {
    return false;
  }

  public boolean isDone ()
  {
    return result != null;
  }

  public GearmanJobResult get () throws InterruptedException, ExecutionException
  {
    return result;
  }

  public GearmanJobResult get (final long timeout, final TimeUnit unit)
          throws InterruptedException, ExecutionException, TimeoutException
  {
    if (result == null) {
      throw new TimeoutException();
    }

    return result;
  }

  public byte[] getHandle ()
  {
    return null;
  }

  public byte[] getID ()
  {
    return null;
  }

  public String getFunctionName ()
  {
    return null;
  }

  public boolean isBackgroundJob ()
  {
    return false;
  }

  public JobPriority getPriority ()
  {
    return null;
  }

  public byte[] getData ()
  {
    return new byte[0];
  }

  public void registerFunction (final Callable<GearmanJobResult> function)
  {
  }

  public void registerEventListener (final GearmanIOEventListener listener)
  {
  }

  public boolean removeEventListener (final GearmanIOEventListener listener)
  {
    return true;
  }

}
