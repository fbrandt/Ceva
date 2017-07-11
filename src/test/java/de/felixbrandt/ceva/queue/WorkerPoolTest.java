package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

import de.felixbrandt.ceva.queue.BaseQueue;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueWorkerFactory;
import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.ceva.queue.WorkerFactory;
import de.felixbrandt.ceva.queue.WorkerPool;

public class WorkerPoolTest
{

  @Test(timeout = 5000)
  public void test ()
  {
    final QueueReader<Job> job_queue = new BaseQueue<Job>(new LinkedList<Job>());
    final QueueWriter<Object> result_queue = new BaseQueue<Object>(new LinkedList<Object>());
    final WorkerFactory factory = new QueueWorkerFactory(job_queue, result_queue);
    final WorkerPool pool = new WorkerPool(factory, 3);

    pool.start();
    assertEquals(true, pool.isRunning());

    pool.start(); // does nothing
    assertEquals(3, pool.activeCount());

    pool.stop();
    assertEquals(false, pool.isRunning());
    pool.stop();
  }

}
