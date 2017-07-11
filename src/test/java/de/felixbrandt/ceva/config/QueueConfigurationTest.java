package de.felixbrandt.ceva.config;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.QueueConfiguration;
import de.felixbrandt.support.ParameterMap;

public class QueueConfigurationTest
{
  Map<String, String> data;
  ParameterMap params;
  QueueConfiguration config;

  @Before
  public void setup ()
  {
    data = new HashMap<String, String>();
    params = new ParameterMap(data);
    config = new QueueConfiguration(params);
  }

  @Test
  public void testWorkerDefault ()
  {
    assertEquals(1, config.getWorkerCount());
  }

  @Test
  public void testWorker ()
  {
    data.put("worker", "2");
    config = new QueueConfiguration(params);
    assertEquals(2, config.getWorkerCount());
  }

  @Test
  public void testModeDefault ()
  {
    assertEquals(true, config.isMaster());
  }

  @Test
  public void testModeSlave ()
  {
    data.put("mode", "slave");
    config = new QueueConfiguration(params);
    assertEquals(false, config.isMaster());
  }

  @Test
  public void testHostDefault ()
  {
    assertEquals("", config.getHost());
  }

  @Test
  public void testHost ()
  {
    data.put("host", "gearman.example.org");
    config = new QueueConfiguration(params);
    assertEquals("gearman.example.org", config.getHost());
  }

  @Test
  public void testPortDefault ()
  {
    assertEquals(4730, config.getPort());
  }

  @Test
  public void testPort ()
  {
    data.put("port", "3333");
    config = new QueueConfiguration(params);
    assertEquals(3333, config.getPort());
  }

  @Test
  public void testJobQueueDefault ()
  {
    assertEquals("ceva", config.getJobQueueName());
  }

  @Test
  public void testJobQueue ()
  {
    data.put("job_queue", "myqueue");
    config = new QueueConfiguration(params);
    assertEquals("myqueue", config.getJobQueueName());
  }

}
