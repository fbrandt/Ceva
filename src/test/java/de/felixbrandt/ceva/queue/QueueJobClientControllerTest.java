package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueJobClientController;

public class QueueJobClientControllerTest
{
  LinkedList<Job> queue;
  QueueJobClientController controller;

  @Before
  public void setUp () throws Exception
  {
    queue = new LinkedList<Job>();
    controller = new QueueJobClientController(new BaseQueueWriter(queue));
  }

  @Test
  public void testSubmit ()
  {
    final MockExecutable executable = new MockExecutable();
    final MockDataSource source = new MockDataSource();

    controller.run(executable, source);
    assertEquals(1, queue.size());
    assertEquals(executable, queue.get(0).getExecutable());
    assertEquals(source, queue.get(0).getSource());
  }

}
