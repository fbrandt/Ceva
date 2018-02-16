package de.felixbrandt.ceva;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.config.ExecutionConfiguration;
import de.felixbrandt.ceva.config.InstanceFilterConfiguration;
import de.felixbrandt.ceva.config.RuleExecutionConfiguration;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.provider.AlgorithmDBProvider;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableFilter;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.provider.HQLFilter;
import de.felixbrandt.ceva.provider.InstanceDBProvider;
import de.felixbrandt.ceva.provider.InstanceMetricDBProvider;
import de.felixbrandt.ceva.provider.SolutionDBProvider;
import de.felixbrandt.ceva.provider.SolutionMetricDBProvider;
import de.felixbrandt.ceva.provider.UnsolvedProvider;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Strategy to execute all missing calculations (metrics and experiments).
 */
public class ExecutionService
{
  private static final Logger LOGGER = LogManager.getLogger();
  private final ExecutionStrategy execution_strategy;

  final ExecutableProvider instance_metric_provider;
  final ExecutableProvider algorithm_provider;
  final ExecutableProvider solution_metric_provider;

  final DataSourceProvider all_instances_provider;
  final DataSourceProvider active_instances_provider;
  final SolutionDBProvider solution_provider;

  final DataDBStorage instance_data_storage;
  final SolutionDBStorage solution_storage;
  final DataDBStorage solution_data_storage;

  public ExecutionService(final SessionHandler session_handler,
          final ExecutionStrategy strategy, final ExecutionConfiguration config)
  {
    execution_strategy = strategy;

    InstanceMetricDBProvider local_instance_metric_provider = new InstanceMetricDBProvider(
            session_handler);
    instance_metric_provider = setupExecutableProvider(config.getInstanceMetricConfiguration(),
            local_instance_metric_provider);
    algorithm_provider = setupExecutableProvider(config.getAlgorithmConfiguration(),
            new AlgorithmDBProvider(session_handler));
    solution_metric_provider = setupExecutableProvider(config.getSolutionMetricConfiguration(),
            new SolutionMetricDBProvider(session_handler));

    instance_data_storage = new InstanceDataDBStorage(session_handler);
    solution_storage = new SolutionDBStorage(session_handler);
    solution_data_storage = new SolutionDataDBStorage(session_handler);

    all_instances_provider = new InstanceDBProvider(session_handler);
    active_instances_provider = setupInstanceProvider(session_handler,
            config.getInstanceFilters(), local_instance_metric_provider);
    solution_provider = new SolutionDBProvider(session_handler);
  }

  public final void run ()
  {
    runInstanceData();
    runAlgorithms();
    runSolutionData();
  }

  public final ExecutableProvider setupExecutableProvider (RuleExecutionConfiguration config,
          ExecutableProvider base_provider)
  {
    if (config.isActive()) {
      return new ExecutableFilter(base_provider, config.getWhitelist(), config.getBlacklist());
    }

    return null;
  }

  public final VersionProvider setupVersionProvider ()
  {
    final CommandFactory command_factory = new ShellCommandFactory();
    return new RunVersionProvider(command_factory);
  }

  private final DataSourceProvider setupInstanceProvider (final SessionHandler session_handler,
          List<InstanceFilterConfiguration> config, InstanceMetricDBProvider provider)
  {
    InstanceFilterBuilder filter_builder = new InstanceFilterBuilder(provider);
    List<HQLFilter> filters = filter_builder.build(config);

    return new InstanceDBProvider(session_handler, filters);
  }

  private final DataSourceFilter setupUnsolvedFilter (UnsolvedProvider provider)
  {
    return new UnsolvedSourcesFilter(provider, setupVersionProvider());
  }

  public final void runAlgorithms ()
  {
    LOGGER.info("starting to execute algorithms");
    doRun(algorithm_provider, active_instances_provider, solution_storage,
            setupUnsolvedFilter(solution_storage));
    LOGGER.info("done with algorithms");
  }

  public final void runInstanceData ()
  {
    LOGGER.info("starting to execute instance metrics");
    doRun(instance_metric_provider, all_instances_provider, instance_data_storage,
            setupUnsolvedFilter(instance_data_storage));
    LOGGER.info("done with instance metrics");
  }

  public final void runSolutionData ()
  {
    LOGGER.info("starting to execute solution metrics");
    doRun(solution_metric_provider, solution_provider, solution_data_storage,
            setupUnsolvedFilter(solution_data_storage));
    LOGGER.info("done with solution metrics");
  }

  public final void doRun (final ExecutableProvider executable_provider,
          final DataSourceProvider source_provider, final Storage storage,
          final DataSourceFilter filter)
  {
    // skip disabled executable providers
    if (executable_provider != null) {
      execution_strategy.run(executable_provider, source_provider, storage, filter);
    } else {
      LOGGER.info("skipping");
    }
  }

}
