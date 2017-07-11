package de.felixbrandt.ceva.provider;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.ceva.provider.InstanceDBProvider;

public class InstanceDBProviderTest
{
  SessionHandler session_handler;
  InstanceDBProvider provider;

  @Before
  public void setUp () throws Exception
  {
    session_handler = TestSessionBuilder.build();
    provider = new InstanceDBProvider(session_handler);
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
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);

    final Collection<? extends DataSource> coll = provider.getDataSources();
    assertEquals(1, coll.size());
    assertEquals(InstanceSource.class, coll.iterator().next().getClass());
  }

  @Test
  public void testGetActiveOnly ()
  {
    final Instance instance = new Instance();
    session_handler.getSession().save(instance);
    final Instance other_instance = new Instance();
    other_instance.setActive(false);
    session_handler.getSession().save(other_instance);

    final Collection<? extends DataSource> coll = provider.getDataSources();
    assertEquals(1, coll.size());
  }

  @Test
  public void testGetByID ()
  {
    final Instance instance = new Instance();
    final Instance other_instance = new Instance();

    session_handler.getSession().save(instance);
    session_handler.getSession().save(other_instance);

    Collection<Instance> coll = null;
    coll = provider.findByKeyword("" + instance.getInstance());
    assertEquals(1, coll.size());
    assertEquals(instance, coll.iterator().next());

    coll = provider.findByKeyword("" + other_instance.getInstance());
    assertEquals(1, coll.size());
    assertEquals(other_instance, coll.iterator().next());

    coll = provider.findByKeyword("42");
    assertEquals(0, coll.size());
  }

  @Test
  public void textGetByName ()
  {
    final Instance instance = new Instance();
    instance.setName("test.txt");
    final Instance other_instance = new Instance();
    other_instance.setName("foobar.txt");

    session_handler.getSession().save(instance);
    session_handler.getSession().save(other_instance);

    Collection<Instance> coll = null;
    coll = provider.findByKeyword("test");
    assertEquals(1, coll.size());
    assertEquals(instance, coll.iterator().next());

    coll = provider.findByKeyword("*.txt");
    assertEquals(2, coll.size());

    coll = provider.findByKeyword("f%bar");
    assertEquals(1, coll.size());

    coll = provider.findByKeyword("f*bar");
    assertEquals(1, coll.size());
  }

  @Test
  public void textGetByIDnotName ()
  {
    final Instance instance = new Instance();
    instance.setName("test.txt");
    session_handler.getSession().save(instance);

    final Instance other_instance = new Instance();
    other_instance.setName("X" + instance.getInstance() + ".txt");
    session_handler.getSession().save(other_instance);

    final Collection<Instance> coll = provider.findByKeyword("1");
    assertEquals(1, coll.size());
    assertEquals(instance, coll.iterator().next());
  }
}
