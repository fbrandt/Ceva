package de.felixbrandt.ceva.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import de.felixbrandt.ceva.controller.base.ContentMode;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.RuleAware;
import de.felixbrandt.support.StringSupport;

/**
 * Wrap Algorithm to act as Executable.
 */
public class AlgorithmExecutable
        implements Executable, RuleAware, java.io.Serializable
{
  private static final long serialVersionUID = 1L;

  private final Algorithm algorithm;
  private Map<String, String> parameters;

  public AlgorithmExecutable(final Algorithm algo)
  {
    algorithm = algo;
    parameters = new HashMap<String, String>();
  }

  public AlgorithmExecutable(final Algorithm algo,
          final Map<String, String> params)
  {
    algorithm = algo;
    setParameters(params);
  }

  public final Algorithm getAlgorithm ()
  {
    return algorithm;
  }

  public final String getFullVersionPath ()
  {
    return algorithm.getFullVersionPath();
  }

  public final String getFullRunPath () throws IllegalArgumentException
  {
    return StringSupport.renderString(algorithm.getFullRunPath(), parameters);
  }

  public final String toString ()
  {
    return "algorithm " + algorithm.getName();
  }

  public final String getName ()
  {
    return algorithm.getName();
  }

  public final ResultFactory getResultFactory ()
  {
    return new SolutionFactory(this);
  }

  public final ContentMode getInputMode ()
  {
    return ContentMode.DEFAULT;
  }

  public static Collection<Executable> generate (
          final Iterable<Algorithm> algorithms)
  {
    final Vector<Executable> result = new Vector<Executable>();

    for (final Algorithm algorithm : algorithms) {
      final List<AlgorithmExecutable> executables = generateAll(algorithm);
      result.addAll(executables);
    }

    return result;
  }

  public static List<AlgorithmExecutable> generateAll (
          final Algorithm algorithm)
  {
    final List<AlgorithmExecutable> executables = new ArrayList<AlgorithmExecutable>();

    if (algorithm.getParameters().isEmpty()) {
      executables.add(new AlgorithmExecutable(algorithm));
      return executables;
    }

    final List<Map<String, String>> parameters = generateParameters(algorithm);
    for (final Map<String, String> map : parameters) {
      executables.add(new AlgorithmExecutable(algorithm, map));
    }

    return executables;
  }

  public static List<Map<String, String>> generateParameters (
          final Algorithm algorithm)
  {
    return StringSupport.generateAllPossibleTuples(algorithm.getParameters());
  }

  public final Algorithm getRule ()
  {
    return algorithm;
  }

  public final int getRepeat ()
  {
    return algorithm.getRepeat();
  }

  public final Map<String, String> getParameters ()
  {
    return parameters;
  }

  public final void setParameters (final Map<String, String> params)
  {
    Map<String, String> filtered_parameters = new HashMap<String, String>();
    Set<String> run_path_tokens = StringSupport
            .getTokens(algorithm.getFullRunPath());

    if (params != null) {
      Iterator<Entry<String, String>> it = params.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, String> param = it.next();
        if (run_path_tokens.contains(param.getKey())) {
          filtered_parameters.put(param.getKey(), param.getValue());
        }
      }
    }
    parameters = filtered_parameters;
  }

  public final String getParametersAsString ()
  {
    return StringSupport.getMapAsString(parameters);
  }

}
