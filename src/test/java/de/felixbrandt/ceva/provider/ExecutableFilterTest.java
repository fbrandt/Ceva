package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.entity.Algorithm;

public class ExecutableFilterTest
{
  class MockExecutableProvider implements ExecutableProvider
  {
    private final Collection<? extends Executable> executables;

    public MockExecutableProvider(Collection<? extends Executable> executables)
    {
      this.executables = executables;
    }

    public Collection<? extends Executable> getExecutables ()
    {
      return executables;
    }
  }

  private List<Executable> executables;
  private MockExecutableProvider provider;
  private ExecutableFilter filter;
  private Algorithm a_algo;
  private Algorithm b_algo;

  @Before
  public void setup ()
  {
    executables = new ArrayList<Executable>();
    provider = new MockExecutableProvider(executables);
    filter = new ExecutableFilter(provider);

    a_algo = new Algorithm();
    a_algo.setName("A-algo");
    b_algo = new Algorithm();
    b_algo.setName("B-algo");

    executables.add(new AlgorithmExecutable(a_algo));
    executables.add(new AlgorithmExecutable(b_algo));
  }

  @Test
  public void testDefault ()
  {
    executables.add(new AlgorithmExecutable(new Algorithm()));
    assertEquals(executables, filter.getExecutables());
  }

  @Test
  public void testWhitelist ()
  {
    List<String> whitelist = new ArrayList<String>();
    whitelist.add("A-algo");

    filter = new ExecutableFilter(provider, whitelist, null);
    Collection<? extends Executable> filtered_algos = filter.getExecutables();
    assertEquals(1, filtered_algos.size());
    assertEquals(a_algo, ((AlgorithmExecutable) filtered_algos.iterator().next()).getRule());
  }

  @Test
  public void testBlacklist ()
  {
    List<String> blacklist = new ArrayList<String>();
    blacklist.add("A-algo");

    filter = new ExecutableFilter(provider, null, blacklist);
    Collection<? extends Executable> filtered_algos = filter.getExecutables();
    assertEquals(1, filtered_algos.size());
    assertEquals(b_algo, ((AlgorithmExecutable) filtered_algos.iterator().next()).getRule());
  }
}
