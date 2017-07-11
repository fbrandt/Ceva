package de.felixbrandt.ceva.init;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.init.AlgorithmService;

public class AlgorithmServiceTest
{
  SessionHandler session_handler;
  AlgorithmService service;

  @Before
  public void setUp ()
  {
    session_handler = TestSessionBuilder.build();
    service = new AlgorithmService(session_handler);
  }

  @After
  public void tearDown ()
  {
    session_handler.shutdown();
  }

  @Test
  public void dbRepeatUpdate ()
  {
    Algorithm algo = new Algorithm();
    algo.setName("algorithm");

    service.update(algo);

    assertEquals(1, service.getByName("algorithm").getRepeat());

    Algorithm update = new Algorithm();
    update.setName("algorithm");
    update.setRepeat(2);

    service.update(update);

    assertEquals(2, service.getByName("algorithm").getRepeat());
  }

}
