package de.felixbrandt.ceva.metric;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.LazySolutionSource;
import de.felixbrandt.ceva.provider.FixedSolutionProvider;
import de.felixbrandt.support.StreamSupport;

public class LazySolutionSourceTest
{
  private Instance instance;
  private Solution solution;
  private FixedSolutionProvider provider;
  private LazySolutionSource source;

  @Before
  public void setup ()
  {
    instance = new Instance();
    solution = new Solution();
    solution.setSolution(42);
    solution.setInstance(instance);
    provider = new FixedSolutionProvider();
    provider.add(solution);
    source = new LazySolutionSource(provider, 42);
  }

  @Test
  public void testGetId ()
  {
    assertEquals(new Integer(42), source.getID());
    // solution was not retrieved yet
    assertEquals(-1, provider.getLastId());
  }
  
  @Test
  public void testGetContent ()
  {
    solution.setStdout("content");
    assertEquals("content", StreamSupport.getStringFromInputStream(source.getContent()));
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
    assertEquals(42, provider.getLastId());
  }

}
