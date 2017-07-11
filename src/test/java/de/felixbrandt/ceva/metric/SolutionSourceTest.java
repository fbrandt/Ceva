package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.SolutionSource;
import de.felixbrandt.support.StreamSupport;

public class SolutionSourceTest
{
  private Instance instance;
  private Solution solution;
  private SolutionSource source;

  @Before
  public void setup ()
  {
    instance = new Instance();
    solution = new Solution();
    solution.setInstance(instance);
    source = new SolutionSource(solution);
  }

  @Test
  public void testGetContent ()
  {
    solution.setStdout("content");
    assertEquals("content", StreamSupport.getStringFromInputStream(source.getContent()));
  }

  @Test
  public void testGetContentStderr ()
  {
    solution.setStderr("errorlog");
    assertEquals("errorlog",
            StreamSupport.getStringFromInputStream(source.getContent(ContentMode.STDERR)));
  }

  @Test
  public void testGetName ()
  {
    instance.setName("filename");
    assertEquals("solution to filename", source.getName());
  }

  @Test
  public void testGetObject ()
  {
    assertEquals(solution, source.getObject());
  }

}
