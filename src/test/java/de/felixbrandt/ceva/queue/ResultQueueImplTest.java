package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.queue.BaseQueueReader;
import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueReaderStatus;
import de.felixbrandt.ceva.queue.QueueWriterStatus;

public class ResultQueueImplTest
{
  QueueWriterStatus<Job> job_queue;
  LinkedList<Object> real_queue;
  QueueReaderStatus<Object> result_queue;

  @Before
  public void setup ()
  {
    job_queue = new QueueWriterStatus<Job>(new BaseQueueWriter<Job>(new LinkedList<Job>()));
    real_queue = new LinkedList<Object>();
    result_queue = new QueueReaderStatus<Object>(job_queue,
            new BaseQueueReader<Object>(real_queue));
  }

  @Test
  public void testHasNextEmpty ()
  {
    assertEquals(true, result_queue.hasNext());
    job_queue.setDone(true);
    assertEquals(false, result_queue.hasNext());
  }

  @Test
  public void testHasNext ()
  {
    job_queue.setDone(true);
    final Object result = new Object();
    real_queue.add(result);

    assertEquals(true, result_queue.hasNext());
    assertEquals(result, result_queue.getNext());

    job_queue.setDone(true);
    assertEquals(false, result_queue.hasNext());
  }

  @Test(timeout = 1000)
  public void testBlocking ()
  {
    class QueueAdder implements Runnable
    {
      Queue queue;
      Object result;

      public QueueAdder(final Queue _queue, final Object _result)
      {
        queue = _queue;
        result = _result;
      }

      public void run ()
      {
        try {
          Thread.sleep(200);
        } catch (final InterruptedException e) {
        }
        LogManager.getLogger().debug("asynchronuous add of result object");
        queue.add(result);
      }
    }

    final Object sample = new Object();
    final QueueAdder adder = new QueueAdder(real_queue, sample);
    new Thread(adder).start();

    final Object result = result_queue.getNext();
    assertEquals(sample, result);
  }
}
