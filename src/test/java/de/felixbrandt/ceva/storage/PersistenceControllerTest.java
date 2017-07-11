package de.felixbrandt.ceva.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockController;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.storage.MockStorage;
import de.felixbrandt.ceva.storage.controller.PersistenceController;

public class PersistenceControllerTest
{
  MockController sub_controller;
  MockStorage storage;
  PersistenceController controller;

  @Before
  public void setup ()
  {
    sub_controller = new MockController(new Integer(23));
    storage = new MockStorage();
    controller = new PersistenceController(sub_controller, storage);
  }

  @Test
  public void testRun ()
  {
    final MockExecutable executable = new MockExecutable();
    final MockDataSource source = new MockDataSource();

    final Object result = controller.run(executable, source);

    assertEquals("result from sub controller returned", sub_controller.getResult(), result);
    assertEquals("sub controller has executable", executable,
            sub_controller.getLastRunExecutable());
    assertEquals("sub controller has source", source, sub_controller.getLastSource());
    assertEquals("result send to storage", sub_controller.getResult(), storage.getLastAdded());
  }

}
