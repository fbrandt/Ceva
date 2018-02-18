package de.felixbrandt.ceva.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ExecutableDecorator;
import de.felixbrandt.ceva.entity.Algorithm;

public class ExecutableDecoratorTest
{
  private Executable exec;

  @Before
  public void setup ()
  {
    Algorithm algorithm = new Algorithm();
    algorithm.setName("test");
    algorithm.setRunPath("runpath");
    algorithm.setVersionPath("versionpath");
    algorithm.setRepeat(23);

    exec = new AlgorithmExecutable(algorithm);
  }

  @Test
  public void testDefault ()
  {
    Executable deco = new ExecutableDecorator(exec);
    assertEquals("test", deco.getName());
    assertEquals("runpath", deco.getFullRunPath());
    assertEquals("versionpath", deco.getFullVersionPath());
    assertEquals(23, deco.getRepeat());
  }

  @Test
  public void testRunPath ()
  {
    Executable deco = new ExecutableDecorator(exec, "myrunpath", null, null);
    assertEquals("myrunpath", deco.getFullRunPath());
  }

  @Test
  public void testVersionPath ()
  {
    Executable deco = new ExecutableDecorator(exec, null, "myversionpath",
            null);
    assertEquals("myversionpath", deco.getFullVersionPath());
  }

  @Test
  public void testRepeat ()
  {
    Executable deco = new ExecutableDecorator(exec, null, null, 24);
    assertEquals(24, deco.getRepeat());
  }
}
