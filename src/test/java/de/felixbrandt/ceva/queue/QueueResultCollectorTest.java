package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueResultCollector;
import de.felixbrandt.ceva.storage.MockStorage;

public class QueueResultCollectorTest
{
  class MockResultQueue implements QueueReader
  {
    public LinkedList<Object> queue;

    public MockResultQueue()
    {
      queue = new LinkedList<Object>();
    }

    public boolean hasNext ()
    {
      return queue.size() > 0;
    }

    public Object getNext ()
    {
      return queue.remove();
    }
  }

  MockStorage storage;
  QueueResultCollector collector;
  MockResultQueue result_queue;

  @Before
  public void setUp () throws Exception
  {
    storage = new MockStorage();
    result_queue = new MockResultQueue();
    collector = new QueueResultCollector(storage, result_queue);
  }

  @Test
  public void testCollectNothing ()
  {
    collector.collect();
    // nothing done
    assertTrue(null == storage.getLastAdded());
  }

  @Test
  public void testCollect ()
  {
    final Object result = new Object();
    result_queue.queue.add(result);

    collector.collect();

    // nothing done
    assertEquals(0, result_queue.queue.size());
    assertTrue(result == storage.getLastAdded());
  }
}
