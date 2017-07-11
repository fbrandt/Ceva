package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.provider.AlgorithmDBProvider;

public class AlgorithmDBProviderTest
{
  SessionHandler session_handler;
  AlgorithmDBProvider provider;

  @Before
  public void setUp () throws Exception
  {
    session_handler = TestSessionBuilder.build();
    provider = new AlgorithmDBProvider(session_handler);
  }

  @After
  public void tearDown () throws Exception
  {
    session_handler.rollback();
  }

  @Test
  public void testGetNone ()
  {
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testGetOne ()
  {
    final Algorithm algo = new Algorithm();
    session_handler.getSession().save(algo);

    final Collection<? extends Executable> coll = provider.getExecutables();
    assertEquals(1, coll.size());
    assertEquals(AlgorithmExecutable.class, coll.iterator().next().getClass());
  }

  @Test
  public void testGetNoInactive ()
  {
    final Algorithm algo = new Algorithm();
    algo.setActive(false);
    session_handler.getSession().save(algo);
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testGetNoInstanceMetrics ()
  {
    final InstanceMetric metric = new InstanceMetric();
    session_handler.getSession().save(metric);
    assertEquals(0, provider.getExecutables().size());
  }

  @Test
  public void testGetByNameMatch ()
  {
    final Algorithm algo = new Algorithm();
    algo.setName("myalgo");
    algo.setActive(false);
    session_handler.getSession().save(algo);

    assertEquals(algo, provider.findByName("myalgo"));
  }

  @Test
  public void testGetByNameMatchInsensitive ()
  {
    final Algorithm algo = new Algorithm();
    algo.setName("myalgo");
    algo.setActive(false);
    session_handler.getSession().save(algo);

    assertEquals(algo, provider.findByName("MYALGO"));
  }

  @Test
  public void testGetByNameFail ()
  {
    assertEquals(null, provider.findByName("myalgo"));
  }

  @Test
  public void testGetByNameFailPartial ()
  {
    final Algorithm algo = new Algorithm();
    algo.setName("myalgo");
    session_handler.getSession().save(algo);

    assertEquals(null, provider.findByName("myalg"));
  }

  @Test
  public void testGetByNameFailAmbiguous ()
  {
    final Algorithm algo1 = new Algorithm();
    algo1.setName("myalgo1");
    session_handler.getSession().save(algo1);
    final Algorithm algo2 = new Algorithm();
    algo2.setName("myalgo2");
    session_handler.getSession().save(algo2);

    assertEquals(null, provider.findByName("myalgo"));
  }

  @Test
  public void testAlgorithmRepeatPersistence ()
  {
    final Algorithm algo = new Algorithm();
    algo.setName("algo");
    session_handler.getSession().save(algo);
    assertEquals(1, provider.findByName("algo").getRepeat());
  }

  @Test
  public void testAlgorithmSerializeParameters ()
  {
    final Algorithm algo = new Algorithm();
    HashMap<String, List<String>> params = new HashMap<String, List<String>>();
    params.put("paramA", Arrays.asList("a", "b"));
    params.put("paramB", Arrays.asList("3"));
    algo.setParameters(params);

    String param_string = algo.getParameterString();
    final Algorithm algo2 = new Algorithm();
    algo2.setParameterString(param_string);
    assertEquals(params, algo2.getParameters());
  }

  @Test
  public void testAlgorithmParameterPersistence ()
  {
    final Algorithm algo = new Algorithm();
    HashMap<String, List<String>> params = new HashMap<String, List<String>>();
    params.put("paramA", Arrays.asList("a", "b"));
    params.put("paramB", Arrays.asList("c"));

    algo.setName("algo");
    algo.setParameters(params);

    session_handler.getSession().save(algo);

    assertEquals(params, provider.findByName("algo").getParameters());
  }
}
