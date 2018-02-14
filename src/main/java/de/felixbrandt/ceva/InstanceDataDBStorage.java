package de.felixbrandt.ceva;

import de.felixbrandt.ceva.database.SessionHandler;

/**
 * Save results of instance metric calculations in database.
 */
public class InstanceDataDBStorage extends DataDBStorage
{

  public InstanceDataDBStorage(final SessionHandler handler)
  {
    super(handler);
  }

  @Override
  public final String getSourceObjectName ()
  {
    return "Instance";
  }

  @Override
  public final String getSourceIDName ()
  {
    return "instance";
  }

}
