package de.felixbrandt.ceva.gearman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueReaderStatus;
import de.felixbrandt.ceva.queue.QueueStatus;

public class QueueReaderStatusTest
{
  class DummyQueueStatus implements QueueStatus
  {
    public int jobs = 0;
    public boolean done = false;

    public int getJobCount ()
    {
      return jobs;
    }

    public void setDone (boolean done)
    {
      this.done = done;
    }

    public boolean isDone ()
    {
      return this.done;
    }

    public boolean isDone (final int actual_count) { return false; }

  };

  class DummyQueueReader implements QueueReader<Object>
  {
    public boolean has_next = false;

    public boolean hasNext ()
    {
      return has_next;
    }

    public Object getNext ()
    {
      return null;
    }

  };

  DummyQueueStatus writer_status;
  DummyQueueReader queue;
  QueueReaderStatus<Object> reader_status;

  @Before
  public void setUp () throws Exception
  {
    writer_status = new DummyQueueStatus();
    queue = new DummyQueueReader();
    reader_status = new QueueReaderStatus<Object>(writer_status, queue);
  }

  @Test
  public void testHasNextNotDone ()
  {
    assertTrue(reader_status.hasNext());
  }

  @Test
  public void testHasNextDoneEmpty ()
  {
    writer_status.setDone(true);
    queue.has_next = true;
    assertTrue(reader_status.hasNext());
  }

  @Test
  public void testHasNextDone ()
  {
    writer_status.jobs = 5;
    writer_status.setDone(true);
    assertTrue(reader_status.hasNext());
  }

  @Test
  public void testHasNextDoneAndRemainingResults ()
  {
    writer_status.jobs = 5;
    writer_status.setDone(true);
    queue.has_next = true;
    assertTrue(reader_status.hasNext());
  }
}
