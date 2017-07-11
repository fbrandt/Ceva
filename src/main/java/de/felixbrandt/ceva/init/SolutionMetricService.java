package de.felixbrandt.ceva.init;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.entity.SolutionMetricFactory;

/**
 * Import/Update solution metrics into database.
 */
public class SolutionMetricService extends RuleService<SolutionMetric>
{

  public SolutionMetricService(final SessionHandler session_handler)
  {
    super(session_handler, new SolutionMetricFactory(), "SolutionMetric");
  }

}
