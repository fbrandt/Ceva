package de.felixbrandt.ceva.gearman;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.gearman.client.GearmanClientImpl;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobResult;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.gearman.GearmanQueueWriter;

public class GearmanQueueWriterTest
{

  class MockGearmanClient extends GearmanClientImpl
  {
    public List<GearmanJob> jobs;
    public boolean crash = false;

    public MockGearmanClient()
    {
      jobs = new ArrayList<GearmanJob>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<T> submit (final Callable<T> task)
    {
      if (crash) {
        throw new RejectedExecutionException("something failed");
      }

      jobs.add((GearmanJob) task);

      return (Future<T>) task;
    }
  }

  MockGearmanClient client;
  List<Future<GearmanJobResult>> result_queue;

  @Before
  public void setUp () throws Exception
  {
    client = new MockGearmanClient();
    result_queue = new ArrayList<Future<GearmanJobResult>>();
  }

  @Test
  public void testAdd ()
  {
    final GearmanQueueWriter<String> queue = new GearmanQueueWriter<String>(client, "test",
            result_queue);
    queue.add("sample");
    assertEquals("job is submitted to GearmanClient", 1, client.jobs.size());
    assertEquals("test", client.jobs.get(0).getFunctionName());
    assertEquals("job result future is send to queue", 1, result_queue.size());
  }

  @Test
  public void testException ()
  {
    final GearmanQueueWriter<String> queue = new GearmanQueueWriter<String>(client, "test",
            result_queue);
    client.crash = true;
    queue.add("sample");
    assertEquals("nothing put into job queue", 0, result_queue.size());
  }
}
