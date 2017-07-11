package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockController;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.QueueResultController;

public class QueueResultControllerTest
{
  MockController sub_controller;
  QueueResultController controller;
  Queue<Object> queue;

  @Before
  public void setUp () throws Exception
  {
    sub_controller = new MockController(new Object());
    queue = new LinkedList<Object>();
    controller = new QueueResultController(sub_controller, new BaseQueueWriter<Object>(queue));
  }

  @Test
  public void test ()
  {
    final MockExecutable executable = new MockExecutable();
    final MockDataSource source = new MockDataSource();

    final Object result = controller.run(executable, source);
    assertTrue("sub controller result is returned", sub_controller.getResult() == result);
    assertEquals(executable, sub_controller.getLastRunExecutable());
    assertEquals(source, sub_controller.getLastSource());
    assertEquals(1, queue.size());
    assertTrue("result sent into queue", result == queue.remove());
  }
}
