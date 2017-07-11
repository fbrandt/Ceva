package de.felixbrandt.ceva.report;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.provider.AlgorithmProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.provider.SolutionProvider;

/**
 * Retrieve solution content.
 */
public class SolutionReport implements GenericReportService, ReportService
{
  private PrintStream out;
  private InstanceProvider instance_provider;
  private AlgorithmProvider algorithm_provider;
  private SolutionProvider solution_provider;

  private static final int ONE_PARAM = 1;
  private static final int TWO_PARAMS = 2;
  private static final int THREE_PARAMS = 3;

  @Parameter(names = { "-s",
      "-solution" }, required = false, description = "ID of the solution.")
  private String solution_id = "";

  @Parameter(names = { "-i",
      "-instance" }, required = false, description = "Keyword for the instance.")
  private String instance_keyword = "*";

  @Parameter(names = { "-a",
      "-algorithm" }, required = false, description = "Name of the algorithm.")
  private String algorithm_name = "";

  @Parameter(names = "-v", required = false, description = "Version of the solution.")
  private String solution_version = "";

  public SolutionReport(final PrintStream _out, final InstanceProvider _instance_provider,
          final AlgorithmProvider _algorithm_provider, final SolutionProvider _provider)
  {
    out = _out;
    instance_provider = _instance_provider;
    algorithm_provider = _algorithm_provider;
    solution_provider = _provider;
  }

  public final  void run ()
  {
    Solution solution = null;

    if (!solution_id.equals("")) {
      solution = run(solution_id);
    } else {
      solution = run(instance_keyword, algorithm_name, solution_version);
    }

    if (solution != null) {
      out.print(solution.getStdout());
    }
  }

  public final Solution run (final String id)
  {
    if (id.matches("\\d+")) {
      return solution_provider.find(Integer.parseInt(id));
    }

    out.println("ERROR: could not parse solution ID: " + id);
    return null;
  }

  public final Solution run (final String instance, final String algorithm)
  {
    if (!instance.equals("") && !algorithm.equals("")) {
      final Instance instance_obj = resolveInstance(instance);
      final Algorithm algorithm_obj = resolveAlgorithm(algorithm);

      if (instance_obj != null && algorithm_obj != null) {
        return processSolutions(solution_provider.find(instance_obj, algorithm_obj));
      }
    }
    return null;
  }

  public final Solution run (final String instance, final String algorithm,
          final String version)
  {
    if (version.equals("")) {
      return run(instance, algorithm);
    }

    if (version.matches("\\d+")) {
      final Instance instance_obj = resolveInstance(instance);
      final Algorithm algorithm_obj = resolveAlgorithm(algorithm);

      if (instance_obj != null && algorithm_obj != null) {
        return processSolutions(solution_provider.find(instance_obj, algorithm_obj,
                Integer.parseInt(version)));
      }
    } else {
      out.println("ERROR: could not parse solution version: " + version);
    }

    return null;
  }

  public final Instance resolveInstance (final String instance)
  {
    final Collection<Instance> result = instance_provider.findByKeyword(instance);

    if (result.size() == 0) {
      out.println("ERROR: instance not found: " + instance);
      return null;
    }
    if (result.size() > 1) {
      out.println("ERROR: instance is ambiguous: " + instance);
      return null;
    }

    return result.iterator().next();
  }

  public final Algorithm resolveAlgorithm (final String algorithm)
  {
    final Algorithm result = algorithm_provider.findByName(algorithm);

    if (result == null) {
      out.println("ERROR: algorithm not found: " + algorithm);
    }

    return result;
  }

  public final Solution processSolutions (final Collection<Solution> solutions)
  {
    if (solutions.isEmpty()) {
      out.println("ERROR: no solutions found");

      return null;
    }

    if (solutions.size() > 1) {
      out.println("ERROR: multiple solutions found: (select one by id or name)");
      out.println("    ID Version Machine          Runtime");
      for (final Solution s : solutions) {
        out.printf("%1$6d %2$7d %3$-16s %4$f%n", s.getSolution(), s.getVersion(),
                s.getMachine(), s.getRuntime());
      }

      return null;
    }

    return solutions.iterator().next();
  }

  public final void run (final List<String> args, final int current_index)
  {
    Solution solution = null;
    final int num_args = args.size() - current_index;
    switch (num_args) {
    case ONE_PARAM:
      solution = run(args.get(current_index));
      break;
    case TWO_PARAMS:
      solution = run(args.get(current_index), args.get(current_index + 1));
      break;
    case THREE_PARAMS:
      solution = run(args.get(current_index), args.get(current_index + 1),
              args.get(current_index + 2));
      break;
    default:
      out.println("ERROR: parameter format unknown");
    }

    if (solution != null) {
      out.print(solution.getStdout());
    }
  }

  public final String getSolutionId ()
  {
    return solution_id;
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
