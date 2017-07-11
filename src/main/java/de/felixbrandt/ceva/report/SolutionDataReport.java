package de.felixbrandt.ceva.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.provider.AlgorithmProvider;
import de.felixbrandt.ceva.provider.DataProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.provider.MetricProvider;
import de.felixbrandt.ceva.provider.SolutionProvider;
import de.felixbrandt.support.RepeatedLogCounter;

/**
 * Retrieve solution metric data.
 */
public class SolutionDataReport extends DataReport
{
  private InstanceProvider instance_provider;
  private AlgorithmProvider algorithm_provider;
  private SolutionProvider solution_provider;

  private static final int INSTANCE_OFFSET = 0;
  private static final int ALGORITHM_OFFSET = 2;
  private static final int VERSION_OFFSET = 3;

  private RepeatedLogCounter repeated_log_counter = new RepeatedLogCounter();

  @Parameter(names = { "-i",
      "-instance" }, required = false, description = "Keyword for the instance.")
  private String instance_keyword = "*";

  @Parameter(names = { "-a",
      "-algorithm" }, required = true, description = "Name of the algorithm.")
  private String algorithm_name = "";

  @Parameter(names = "-v", required = false, description = "Version of the solution.")
  private String solution_version = "";

  public SolutionDataReport(final PrintStream _out, final InstanceProvider _instance_provider,
          final AlgorithmProvider _algorithm_provider,
          final SolutionProvider _solution_provider, final MetricProvider _metric_provider,
          final DataProvider _data_provider)
  {
    super(_out, _metric_provider, _data_provider);
    instance_provider = _instance_provider;
    algorithm_provider = _algorithm_provider;
    solution_provider = _solution_provider;
  }

  @Override
  public final void printHeader (final PrintStream out)
  {
    out.printf("%s;%s;%s;%s;%s;%s;%s;%s%n", "ID", "INSTANCE", "ALGORITHM", "PARAMETERS",
            "VERSION", "METRIC", "VALUE", "REPEAT");
  }

  @Override
  public final void printRow (final PrintStream out, final Data<?, ?> data)
  {
    final Solution solution = (Solution) data.getSource();
    final Metric metric = (Metric) data.getRule();

    String log_string = solution.getInstance().getName() + " "
            + solution.getAlgorithm().getName() + " " + solution.getParameters() + " "
            + solution.getVersion() + " " + metric.getName();
    repeated_log_counter.addLog(log_string);

    out.printf("%d;%s;%s;%s;%d;%s;%s;%s%n", solution.getSolution(),
            solution.getInstance().getName(), solution.getAlgorithm().getName(),
            solution.getParameters(), solution.getVersion(), metric.getName(),
            data.getValue().toString(), repeated_log_counter.countReptitionsOfLog(log_string));
  }

  @Override
  public final Collection<?> retrieveSources ()
  {
    final Collection<Instance> instances = instance_provider.findByKeyword(instance_keyword);
    final Algorithm algorithm = algorithm_provider.findByName(algorithm_name);

    if (algorithm == null) {
      getStream().println("ERROR: algorithm " + algorithm_name + " not found");
      return null;
    }

    try {
      return retrieveSolutions(instances, algorithm, Integer.parseInt(solution_version));
    } catch (NumberFormatException e) {
      return retrieveSolutions(instances, algorithm);
    }

  }

  public final Collection<Solution> retrieveSolutions (final Collection<Instance> instances,
          final Algorithm algorithm, final int version)
  {
    final ArrayList<Solution> solutions = new ArrayList<Solution>();

    for (final Instance instance : instances) {
      solutions.addAll(solution_provider.find(instance, algorithm, version));
    }

    return solutions;
  }

  public final Collection<Solution> retrieveSolutions (final Collection<Instance> instances,
          final Algorithm algorithm)
  {
    final ArrayList<Solution> solutions = new ArrayList<Solution>();

    for (final Instance instance : instances) {
      solutions.addAll(solution_provider.find(instance, algorithm));
    }

    return solutions;
  }

  @Override
  public final Collection<?> retrieveSources (final List<String> args, final int current_index)
  {
    if (args.size() <= current_index + VERSION_OFFSET) {
      getStream().println("ERROR: not enough arguments");

      return null;
    }
    final Collection<Instance> instances = instance_provider
            .findByKeyword(args.get(current_index + INSTANCE_OFFSET));

    final String algorithm_name_from_args = args.get(current_index + ALGORITHM_OFFSET);
    final Algorithm algorithm = algorithm_provider.findByName(algorithm_name_from_args);
    if (algorithm == null) {
      getStream().println("ERROR: algorithm " + algorithm_name_from_args + " not found");

      return null;
    }

    try {
      final int version = Integer.parseInt(args.get(current_index + VERSION_OFFSET));
      return retrieveSolutions(instances, algorithm, version);
    } catch (final NumberFormatException e) {
      getStream().println(
              "ERROR: could not parse version:" + args.get(current_index + VERSION_OFFSET));
      return null;
    }
  }

  public final String getInstanceKeyword ()
  {
    return instance_keyword;
  }

  public final String getAlgorithmName ()
  {
    return algorithm_name;
  }

  public final String getSolutionVersion ()
  {
    return solution_version;
  }
}
