package de.felixbrandt.ceva;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.autoscale.AutoScaleManager;
import de.felixbrandt.ceva.config.Configuration;
import de.felixbrandt.ceva.config.QueueConfiguration;
import de.felixbrandt.ceva.database.HibernateConfigurationBuilder;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.gearman.GearmanWorkerFactory;
import de.felixbrandt.ceva.queue.WorkerPool;
import de.felixbrandt.ceva.report.ReportService;
import de.felixbrandt.support.StreamSupport;

/**
 * Program entry point.
 */
public class Main
{

  private static final Logger LOGGER = LogManager.getLogger();
  private static final long GSERVICE_CYCLE_WAIT_TIME = 1000;

  protected Main()
  {
  }

  public static void main (final String[] args) throws Exception
  {
    InputStream config_stream = StreamSupport.createEmptyInputStream();
    if (args.length > 0) {
      config_stream = new FileInputStream(args[0]);
    } else {
      final File file = new File("ceva.yaml");
      if (file.canRead()) {
        config_stream = new FileInputStream(file.getPath());
      }
    }

    java.util.logging.Logger.getLogger("org.hibernate")
            .setLevel(java.util.logging.Level.SEVERE);
    final Configuration config = new Configuration(config_stream);

    if (args.length > 1) {
      final SessionHandler session_handler = setupSessionHandler(config);
      final ReportService report_service = CevaReportServiceFactory.setup(session_handler,
              System.in, System.out);
      report_service.run(Arrays.asList(args), 1);
      session_handler.rollback();
    } else {
      runExperiments(config);
    }

    LOGGER.info("done");

    System.exit(0);
  }

  public static SessionHandler setupSessionHandler (final Configuration config)
          throws Exception
  {
    final SessionHandler session_handler = CevaSessionBuilder
            .build(new HibernateConfigurationBuilder().create(config.getDatabaseConfig()));
    session_handler.setup();

    return session_handler;
  }

  public static void runExperiments (final Configuration config)
  {
    AutoScaleManager autoscale_manager = null;
    Thread autoscale_thread = null;

    if (config.getAutoScaleConfig().isActive()) {
      autoscale_manager = new AutoScaleManager(config.getAutoScaleConfig(),
              config.getQueueConfig());
      autoscale_thread = new Thread(autoscale_manager);
      autoscale_thread.start();
    }
    SessionHandler session_handler = null;
    WorkerPool gearman_worker_pool = null;

    try {
      if (!config.getQueueConfig().getHost().equals("")) {
        final QueueConfiguration queue_config = config.getQueueConfig();
        gearman_worker_pool = new WorkerPool(new GearmanWorkerFactory(queue_config),
                queue_config.getWorkerCount());
        gearman_worker_pool.start();
      }

      if (config.getQueueConfig().isMaster()) {
        session_handler = setupSessionHandler(config);
        ImportService.run(session_handler, config);
        final ExecutionStrategy strategy;

        if (!config.getQueueConfig().getHost().equals("")) {
          strategy = new GearmanExecutionStrategy(config.getQueueConfig());
        } else {
          if (config.getQueueConfig().getWorkerCount() > 1) {
            strategy = new WorkerExecutionStrategy(config.getQueueConfig().getWorkerCount());
          } else {
            strategy = new SynchronuousExecutionStrategy();
          }
        }

        LOGGER.info("selected strategy: {}", strategy.getClass().getSimpleName());
        final ExecutionService service = new ExecutionService(session_handler, strategy);
        service.run();
        session_handler.commit();
      } else if (gearman_worker_pool != null) {
        while (gearman_worker_pool.isRunning()) {
          Thread.sleep(GSERVICE_CYCLE_WAIT_TIME);
        }
      }

      if (gearman_worker_pool != null) {
        gearman_worker_pool.stop();
      }
    } catch (final Exception e) {
      e.printStackTrace();
      if (session_handler != null) {
        session_handler.rollback();
      }
    }

    if (autoscale_manager != null) {
      autoscale_manager.stop();
      try {
        autoscale_thread.join();
      } catch (InterruptedException e) {
        LOGGER.warn(e.getMessage());
      }
    }
  }
}
