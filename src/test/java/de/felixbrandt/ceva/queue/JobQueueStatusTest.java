package de.felixbrandt.ceva.queue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueWriterStatus;

import static org.junit.Assert.*;

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
  public void test () throws Exception
  {
    queue.add(new Job(null, null));
    assertEquals(1, queue.getJobCount());
    assertEquals(1, wrapped_queue.size());
  }

  @Test
  public void testFailAddingAfterDone ()
  {
    queue.setDone(true);
    assertFalse(queue.add(new Job(null, null)));
  }

  @Test
  public void testDone ()
  {
    assertFalse(queue.isDone());
    queue.setDone(true);
    assertTrue(queue.isDone());
  }

  @Test
  public void testDoneCount () throws Exception
  {
    queue.add(new Job(null, null));
    queue.setDone(true);
    assertFalse(queue.isDone(0));
    assertTrue(queue.isDone(1));
    assertTrue(queue.isDone(2));
  }
}
