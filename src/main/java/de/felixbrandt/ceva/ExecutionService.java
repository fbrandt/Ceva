package de.felixbrandt.ceva;

import java.util.List;

import de.felixbrandt.ceva.config.ExecutionConfiguration;
import de.felixbrandt.ceva.config.InstanceFilterConfiguration;
import de.felixbrandt.ceva.controller.CommandFactory;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.controller.RunVersionProvider;
import de.felixbrandt.ceva.controller.ShellCommandFactory;
import de.felixbrandt.ceva.controller.base.VersionProvider;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.provider.AlgorithmDBProvider;
import de.felixbrandt.ceva.provider.DataSourceProvider;
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
  private final ExecutionStrategy execution_strategy;

  final InstanceMetricDBProvider instance_metric_provider;
  final SolutionMetricDBProvider solution_metric_provider;
  final DataSourceProvider all_instances_provider;

  final AlgorithmDBProvider algorithm_provider;
  final DataSourceProvider active_instances_provider;
  final SolutionDBProvider solution_provider;

  final DataDBStorage instance_data_storage;
  final SolutionDBStorage solution_storage;
  final DataDBStorage solution_data_storage;

  public ExecutionService(final SessionHandler session_handler,
          final ExecutionStrategy strategy, final ExecutionConfiguration config)
  {
    execution_strategy = strategy;

    instance_metric_provider = new InstanceMetricDBProvider(session_handler);
    algorithm_provider = new AlgorithmDBProvider(session_handler);
    solution_metric_provider = new SolutionMetricDBProvider(session_handler);

    instance_data_storage = new InstanceDataDBStorage(session_handler);
    solution_storage = new SolutionDBStorage(session_handler);
    solution_data_storage = new SolutionDataDBStorage(session_handler);

    all_instances_provider = new InstanceDBProvider(session_handler);
    active_instances_provider = setupInstanceProvider(session_handler,
            config.getInstanceFilters());
    solution_provider = new SolutionDBProvider(session_handler);
  }

  public final void run ()
  {
    runInstanceData();
    runAlgorithms();
    runSolutionData();
  }

  public final VersionProvider setupVersionProvider ()
  {
    final CommandFactory command_factory = new ShellCommandFactory();
    return new RunVersionProvider(command_factory);
  }

  private final DataSourceProvider setupInstanceProvider (final SessionHandler session_handler,
          List<InstanceFilterConfiguration> config)
  {
    InstanceFilterBuilder filter_builder = new InstanceFilterBuilder(instance_metric_provider);
    List<HQLFilter> filters = filter_builder.build(config);

    return new InstanceDBProvider(session_handler, filters);
  }

  private final DataSourceFilter setupUnsolvedFilter (UnsolvedProvider provider)
  {
    return new UnsolvedSourcesFilter(provider, setupVersionProvider());
  }

  public final void runAlgorithms ()
  {
    doRun(algorithm_provider, active_instances_provider, solution_storage,
            setupUnsolvedFilter(solution_storage));
  }

  public final void runInstanceData ()
  {
    doRun(instance_metric_provider, all_instances_provider, instance_data_storage,
            setupUnsolvedFilter(instance_data_storage));
  }

  public final void runSolutionData ()
  {
    doRun(solution_metric_provider, solution_provider, solution_data_storage,
            setupUnsolvedFilter(solution_data_storage));
  }

  public final void doRun (final ExecutableProvider executable_provider,
          final DataSourceProvider source_provider, final Storage storage,
          final DataSourceFilter filter)
  {
    execution_strategy.run(executable_provider, source_provider, storage, filter);
  }

}
