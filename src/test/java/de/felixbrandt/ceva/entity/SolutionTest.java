package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Solution;

public class SolutionTest
{

  private Solution solution;

  @Before
  public void setUp ()
  {
    solution = new Solution();
  }

  @Test
  public void testDefaultParameters ()
  {
    assertEquals(0, solution.getSolution());
    assertEquals(null, solution.getAlgorithm());
    assertEquals(null, solution.getInstance());
    assertEquals(0, solution.getVersion());
    assertEquals("", solution.getStdout());
    assertEquals("", solution.getStderr());
    assertEquals(-1.0, solution.getRuntime(), 0.1);
    assertEquals("", solution.getParameters());
  }
}
