package de.felixbrandt.ceva.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.BatchController;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.MockController;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

public class BatchControllerTest
{

  MockController wrapped_controller;
  BatchController controller;
  Vector<Executable> executables;
  Vector<DataSource> sources;

  @Before
  public void setup ()
  {
    wrapped_controller = new MockController();
    controller = new BatchController(wrapped_controller);

    executables = new Vector<Executable>();
    executables.add(new MockExecutable());
    executables.add(new MockExecutable());

    sources = new Vector<DataSource>();
    sources.add(new MockDataSource());
    sources.add(new MockDataSource());
  }

  @Test
  public void testRunExecutablesSources ()
  {
    controller.run(executables, sources);
    assertEquals(4, wrapped_controller.getCallCount());
    assertEquals(executables.get(1), wrapped_controller.getLastRunExecutable());
    assertEquals(sources.get(1), wrapped_controller.getLastSource());
  }

  @Test
  public void testRunExecutableSources ()
  {
    controller.run(executables.get(0), sources);
    assertEquals(2, wrapped_controller.getCallCount());
  }

  @Test
  public void testRunExecutablesSource ()
  {
    controller.run(executables, sources.get(0));
    assertEquals(2, wrapped_controller.getCallCount());
  }

  @Test
  public void testRunExecutableSource ()
  {
    controller.run(executables.get(0), sources.get(0));
    assertEquals(1, wrapped_controller.getCallCount());
  }

  class MockFilter implements DataSourceFilter
  {
    public Collection<? extends DataSource> doFilter (final Executable executable,
            final Collection<? extends DataSource> sources)
    {
      final ArrayList<DataSource> result = new ArrayList<DataSource>();
      result.add(sources.iterator().next());
      return result;
    }
  }

  @Test
  public void testRunFiltered ()
  {
    controller = new BatchController(wrapped_controller, new MockFilter());
    controller.runFiltered(executables.get(0), sources);
    assertEquals(1, wrapped_controller.getCallCount());
  }

  @Test
  public void testRunAllFiltered ()
  {
    controller.setFilter(new MockFilter());
    controller.run(executables, sources);
    assertEquals(2, wrapped_controller.getCallCount());
  }

}
