package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.LazySolutionSource;
import de.felixbrandt.ceva.provider.SolutionDBProvider;

public class SolutionDBProviderTest
{
  SessionHandler session_handler;
  SolutionDBProvider provider;

  @Before
  public void setUp () throws Exception
  {
    session_handler = TestSessionBuilder.build();
    provider = new SolutionDBProvider(session_handler);
  }

  @After
  public void tearDown () throws Exception
  {
    session_handler.rollback();
  }

  @Test
  public void testGetNone ()
  {
    assertEquals(0, provider.getDataSources().size());
  }

  @Test
  public void testGet ()
  {
    final Solution solution = new Solution();
    session_handler.getSession().save(solution);

    final Collection<? extends DataSource> coll = provider.getDataSources();
    assertEquals(1, coll.size());
    assertEquals(LazySolutionSource.class, coll.iterator().next().getClass());
  }

  @Test
  public void testFindByID ()
  {
    final Solution solution = new Solution();
    session_handler.getSession().save(solution);

    assertTrue(solution.getSolution() != 0);
    assertEquals(solution.getSolution(), provider.find(solution.getSolution()).getSolution());
    assertEquals(null, provider.find(-42));
  }

  @Test
  public void testFindByParams ()
  {
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);
    final Algorithm algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);
    final Solution solution = new Solution(instance, algorithm, 42);
    session_handler.getSession().save(solution);

    final Collection<Solution> full_match = provider.find(instance, algorithm, 42);
    assertTrue(full_match.contains(solution));

    assertEquals(0, provider.find(instance, algorithm, 21).size());
  }

  @Test
  public void testFindByParamsLast ()
  {
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);
    final Algorithm algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);
    final Solution solution_a = new Solution(instance, algorithm, 10);
    session_handler.getSession().save(solution_a);
    final Solution solution_b = new Solution(instance, algorithm, 30);
    session_handler.getSession().save(solution_b);
    final Solution solution_c = new Solution(instance, algorithm, 20);
    session_handler.getSession().save(solution_c);

    final Collection<Solution> latest_version_match = provider.find(instance, algorithm);
    assertEquals(1, latest_version_match.size());
    assertTrue(latest_version_match.contains(solution_b));
  }

  @Test
  public void testFindByParamsNone ()
  {
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);
    final Algorithm algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);

    final Collection<Solution> latest_version_match = provider.find(instance, algorithm);
    assertEquals(0, latest_version_match.size());
  }

  @Test
  public void testSolutionWithNullAsParameters ()
  {
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);
    final Algorithm algorithm = new Algorithm();
    session_handler.getSession().save(algorithm);

    Solution old_database_solution = new Solution(instance, algorithm, 0);
    old_database_solution.setParameters(null);

    session_handler.getSession().save(old_database_solution);

    final Collection<Solution> old_database_entries = provider.find(instance, algorithm);
    assertEquals("", old_database_entries.iterator().next().getParameters());
  }

}
