package de.felixbrandt.ceva;

import de.felixbrandt.ceva.database.SessionHandler;

/**
 * Save results of solution metric calculations in database.
 */
public class SolutionDataDBStorage extends DataDBStorage
{

  public SolutionDataDBStorage(final SessionHandler handler)
  {
    super(handler);
  }

  @Override
  public final String getSourceObjectName ()
  {
    return "Solution";
  }

  @Override
  public final String getSourceIDName ()
  {
    return "solution";
  }

}
