package de.felixbrandt.ceva.init;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.InstanceMetricFactory;

/**
 * Import/Update instance metrics into database.
 */
public class InstanceMetricService extends RuleService<InstanceMetric>
{

  public InstanceMetricService(final SessionHandler session_handler)
  {
    super(session_handler, new InstanceMetricFactory(), "InstanceMetric");
  }

}
