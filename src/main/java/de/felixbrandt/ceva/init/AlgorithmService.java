package de.felixbrandt.ceva.init;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.AlgorithmFactory;

/**
 * Import/Update algorithms into database.
 */
public class AlgorithmService extends RuleService<Algorithm>
{

  public AlgorithmService(final SessionHandler session_handler)
  {
    super(session_handler, new AlgorithmFactory(), "Algorithm");
  }

}
