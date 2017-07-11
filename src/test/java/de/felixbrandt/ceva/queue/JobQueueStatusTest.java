package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueWriterStatus;

public class JobQueueStatusTest
{
  Queue<Job> wrapped_queue;
  QueueWriterStatus queue;

  @Before
  public void setUp () throws Exception
  {
    wrapped_queue = new LinkedList<Job>();
    queue = new QueueWriterStatus(new BaseQueueWriter(wrapped_queue));
  }

  @Test
  public void test ()
  {
    queue.add(new Job(null, null));
    assertEquals(1, queue.getJobCount());
    assertEquals(1, wrapped_queue.size());
  }

  @Test
  public void testDone ()
  {
    assertEquals(false, queue.isDone());
    queue.setDone(true);
    assertEquals(true, queue.isDone());
  }
}
