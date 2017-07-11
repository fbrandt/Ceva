package de.felixbrandt.ceva.config;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import de.felixbrandt.ceva.entity.AlgorithmFactory;
import de.felixbrandt.ceva.entity.InstanceMetricFactory;
import de.felixbrandt.ceva.entity.SolutionMetricFactory;
import de.felixbrandt.support.ParameterMap;

/**
 * Application configuration (database, instances, rules)
 */
public class Configuration
{
  private static final Logger LOGGER = LogManager.getLogger();
  private ParameterMap params;

  @SuppressWarnings("unchecked")
  public Configuration(final InputStream in)
  {
    final Yaml yaml = new Yaml();
    params = new ParameterMap((Map<String, ?>) yaml.load(in));
  }

  public final DBConfiguration getDatabaseConfig ()
  {
    return new DBConfiguration(getDatabaseParams());
  }

  public final ParameterMap getDatabaseParams ()
  {
    return new ParameterMap(doGetDatabaseParams());
  }

  public final Map<String, ?> doGetDatabaseParams ()
  {
    return params.getMapParam("database");
  }

  public final InstanceConfiguration getInstanceConfig ()
  {
    return new InstanceConfiguration(getInstanceParams());
  }

  public final List<?> getInstanceParams ()
  {
    return params.getListParam("instances");
  }

  public final RuleConfiguration getInstanceMetrics ()
  {
    final Map<String, ?> metric_params = getInstanceMetricParams();
    LOGGER.debug("found " + metric_params.size() + " instance metrics in configuration");

    return new RuleConfiguration(new InstanceMetricFactory(), metric_params);
  }

  public final Map<String, ?> getInstanceMetricParams ()
  {
    return params.getMapParam("imetrics");
  }

  public final RuleConfiguration getAlgorithms ()
  {
    final Map<String, ?> algo_params = getAlgorithmParams();
    LOGGER.debug("found " + algo_params.size() + " algorithms in configuration");

    return new RuleConfiguration(new AlgorithmFactory(), algo_params);
  }

  public final Map<String, ?> getAlgorithmParams ()
  {
    return params.getMapParam("algorithms");
  }

  public final RuleConfiguration getSolutionMetrics ()
  {
    final Map<String, ?> metric_params = getSolutionMetricParams();
    LOGGER.debug("found " + metric_params.size() + " solution metrics in configuration");

    return new RuleConfiguration(new SolutionMetricFactory(), metric_params);
  }

  public final Map<String, ?> getSolutionMetricParams ()
  {
    if (params.has("smetrics")) {
      return params.getMapParam("smetrics");
    }

    // fallback to ometrics
    return params.getMapParam("ometrics");
  }

  public final ParameterMap getQueueParams ()
  {
    return new ParameterMap(doGetQueueParams());
  }

  public final Map<String, ?> doGetQueueParams ()
  {
    return params.getMapParam("queue");
  }

  public final QueueConfiguration getQueueConfig ()
  {
    return new QueueConfiguration(getQueueParams());
  }

}
