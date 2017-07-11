package de.felixbrandt.ceva;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.provider.AlgorithmProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.report.ConfigurableReportService;
import de.felixbrandt.ceva.storage.base.Storage;
import de.felixbrandt.support.StreamSupport;

/**
 * Import externally created solutions into CEVA.
 */
public class SolutionImportService implements ConfigurableReportService<SolutionImportConfig>
{
  private PrintStream out;
  private InstanceProvider instance_provider;
  private AlgorithmProvider algo_provider;
  private Storage storage;

  public SolutionImportService(final PrintStream _out,
          final InstanceProvider _instance_provider,
          final AlgorithmProvider _algorithm_provider, final Storage _storage)
  {
    out = _out;
    instance_provider = _instance_provider;
    algo_provider = _algorithm_provider;
    storage = _storage;
  }

  public final void run (final SolutionImportConfig config)
  {
    final Instance instance = resolveInstance(config.getInstanceKeyword());
    final Algorithm algorithm = resolveAlgorithm(config.getAlgorithmName());
    final int version = config.getVersion();
    final Solution solution = createSolution(instance, algorithm, version, config);

    if (instance != null && algorithm != null) {
      storage.add(solution);
      out.printf("DONE: imported solution to %1$s for instance %2$s%n",
              solution.getAlgorithm().getName(), solution.getInstance().getName());
    } else {
      out.println("ERROR: nothing imported");
    }
  }

  public final Solution createSolution (final Instance instance, final Algorithm algorithm,
          final int version, final SolutionImportConfig config)
  {
    final Solution solution = new Solution();
    solution.setInstance(instance);
    solution.setAlgorithm(algorithm);
    solution.setVersion(version);
    solution.setMachine(config.getMachine());
    solution.setRuntime(config.getRuntime());
    solution.setParameters(config.getParams());

    try {
      solution.setStdout(StreamSupport.getStringFromInputStream(config.getStdoutStream()));
    } catch (FileNotFoundException e) {
      out.println("ERROR: stdout file not found");
      return null;
    }

    try {
      solution.setStderr(StreamSupport.getStringFromInputStream(config.getStderrStream()));
    } catch (FileNotFoundException e) {
      out.println("ERROR: stderr file not found");
      return null;
    }

    return solution;
  }

  public final Instance resolveInstance (final String instance_keyword)
  {
    if (instance_keyword == null || instance_keyword.equals("")) {
      out.println("ERROR: no instance specified");
      return null;
    }

    final Collection<Instance> result = instance_provider.findByKeyword(instance_keyword);

    if (result.size() == 0) {
      out.printf("ERROR: instance %1$s not found%n", instance_keyword);
      return null;
    }
    if (result.size() > 1) {
      out.printf("ERROR: instance %1$s is ambiguous%n", instance_keyword);
      return null;
    }

    return (Instance) result.toArray()[0];
  }

  public final Algorithm resolveAlgorithm (final String algorithm_name)
  {
    if (algorithm_name == null || algorithm_name.equals("")) {
      out.println("ERROR: no algorithm specified");
      return null;
    }

    final Algorithm algorithm = algo_provider.findByName(algorithm_name);
    if (algorithm == null) {
      out.printf("ERROR: algorithm %1$s not found%n", algorithm_name);
      return null;
    }

    return algorithm;
  }

}
