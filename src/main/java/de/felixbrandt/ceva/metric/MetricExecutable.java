package de.felixbrandt.ceva.metric;

import java.util.HashMap;

import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.RuleAware;

/**
 * Wrap Metric to act as Executable.
 */
public abstract class MetricExecutable<MetricType extends Metric> implements RuleAware,
        Executable, java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private MetricType metric;

  public MetricExecutable(final MetricType _metric)
  {
    metric = _metric;
  }

  public final MetricType getMetric ()
  {
    return metric;
  }

  public final String getFullVersionPath ()
  {
    return metric.getFullVersionPath();
  }

  public final String getFullRunPath ()
  {
    return metric.getFullRunPath();
  }

  public final String getName ()
  {
    return "metric " + metric.getName();
  }

  public abstract ResultFactory getResultFactory ();

  public final Metric getRule ()
  {
    return metric;
  }

  public final int getRepeat ()
  {
    return 1;
  }

  public final HashMap<String, String> getParameters ()
  {
    return new HashMap<String, String>();
  }

  public final String getParametersAsString ()
  {
    return "";
  }
}
