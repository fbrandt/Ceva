package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueJobWorker;
import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueWorkerFactory;
import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.ceva.queue.WorkerFactory;

public class QueueWorkerFactoryTest
{
  @Test
  public void test ()
  {
    final QueueReader<Job> job_queue = null;
    final QueueWriter<Object> result_queue = null;

    final WorkerFactory factory = new QueueWorkerFactory(job_queue, result_queue);
    final Runnable worker = factory.create();
    assertTrue(worker instanceof QueueJobWorker);
  }
}
