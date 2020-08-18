package de.felixbrandt.ceva;

import de.felixbrandt.ceva.config.Configuration;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.init.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Strategy updating the database with the current application configuration.
 */
public class ImportService
{

  protected ImportService()
  {
  }

  public static void run (final SessionHandler session_handler,
          final Configuration config)
  {
    InstanceImportService.run(session_handler,
            config.getInstanceConfig().getInstances(),
            config.getIdentifyInstancesByName());
    session_handler.saveAndBegin();

    final InstanceMetricService instance_metric_import = new InstanceMetricService(
            session_handler);
    instance_metric_import.update(config.getInstanceMetrics().getRules());

    final AlgorithmService algorithm_import = new AlgorithmService(
            session_handler);
    algorithm_import.update(config.getAlgorithms().getRules());

    final SolutionMetricService solution_metric_import = new SolutionMetricService(
            session_handler);
    solution_metric_import.update(config.getSolutionMetrics().getRules());
    session_handler.saveAndBegin();

    final SolutionViewService view_service = new SolutionViewService(session_handler);
    final Collection<Metric> metrics = new ArrayList<Metric>();
    metrics.addAll(instance_metric_import.getAll());
    metrics.addAll(solution_metric_import.getAll());
    view_service.updateView(metrics);
    session_handler.saveAndBegin();
  }

}
