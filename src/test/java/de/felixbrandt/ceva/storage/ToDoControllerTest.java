package de.felixbrandt.ceva.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockController;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.controller.MockVersionProvider;
import de.felixbrandt.ceva.storage.MockStorage;
import de.felixbrandt.ceva.storage.controller.ToDoController;

public class ToDoControllerTest
{
  MockExecutable executable;
  MockDataSource source;
  MockController sub_controller;
  MockStorage storage;
  MockVersionProvider version_provider;
  ToDoController controller;

  @Before
  public void setUp () throws Exception
  {
    executable = new MockExecutable();
    source = new MockDataSource();

    sub_controller = new MockController(new Integer(23));
    storage = new MockStorage();
    version_provider = new MockVersionProvider(42);
    controller = new ToDoController(sub_controller, storage, version_provider);
  }

  @Test
  public void testToDo ()
  {
    storage.setExists(false);

    final Object result = controller.run(executable, source);

    assertEquals("result from sub controller returned", sub_controller.getResult(), result);
    assertEquals("sub controller called", 1, sub_controller.getCallCount());
    assertEquals("sub controller has executable", executable,
            sub_controller.getLastRunExecutable());
    assertEquals("sub controller has source", source, sub_controller.getLastSource());
    assertEquals("nothing added to storage", 0, storage.getAddCount());
  }

  @Test
  public void testNotToDo ()
  {
    storage.setExists(true);
    storage.setCount(executable.getRepeat());

    controller.run(executable, source);

    assertEquals("sub controller not called", 0, sub_controller.getCallCount());
    assertEquals("nothing added to storage", 0, storage.getAddCount());
  }

  @Test
  public void testToDoRepeat ()
  {
    storage.setExists(false);
    storage.setCount(0);
    executable.setRepeat(2);

    controller.run(executable, source);

    assertEquals("todo controller repeats calculation 2 times", 2,
            sub_controller.getCallCount());
  }

  @Test
  public void testToDoPending ()
  {
    storage.setExists(true);
    storage.setCount(2);
    executable.setRepeat(5);

    controller.run(executable, source);

    assertEquals("todo controller completes 3 pending calculation", 3,
            sub_controller.getCallCount());

  }

  @Test
  public void testToDoRepeatZero ()
  {
    storage.setExists(false);
    storage.setCount(0);
    executable.setRepeat(0);

    controller.run(executable, source);

    assertEquals("todo controller does no calculation when repeat set to 0",
            sub_controller.getCallCount(), 0);
  }
}
