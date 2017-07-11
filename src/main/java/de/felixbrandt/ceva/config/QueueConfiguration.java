package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

/**
 * Application worker queue configuration.
 */
public class QueueConfiguration
{
  private static final int GEARMAN_DEFAULT_PORT = 4730;
  private int workers;
  private String mode;
  private String host;
  private int port;
  private String job_queue;

  public QueueConfiguration(final ParameterMap params)
  {
    workers = params.getIntParam("worker", 1);
    mode = params.getStringParam("mode", "masterslave");
    host = params.getStringParam("host");
    port = params.getIntParam("port", GEARMAN_DEFAULT_PORT);

    job_queue = params.getStringParam("job_queue", "ceva");
  }

  public final int getWorkerCount ()
  {
    return workers;
  }

  public final boolean isMaster ()
  {
    return !mode.equals("slave");
  }

  public final String getHost ()
  {
    return host;
  }

  public final int getPort ()
  {
    return port;
  }

  public final String getJobQueueName ()
  {
    return job_queue;
  }
}
