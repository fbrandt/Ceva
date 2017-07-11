package de.felixbrandt.ceva.report;

import com.beust.jcommander.Parameter;

/**
 * Helper class to mock generic report services for unit tests.
 */
public class MockGenericReportService implements GenericReportService
{
  @Parameter(names = "-noDefaultAndNonRequired", required = false,
          description = "Non required parameter without default value.")
  private String no_default_and_non_required;

  @Parameter(names = "-noDefaultAndRequired", required = true,
          description = "Required parameter without default value.")
  private String no_default_and_required;

  @Parameter(names = "-defaultAndNonRequired", required = false,
          description = "Non required parameter with default value.")
  private String default_and_non_required = "defaultAndNonRequired";

  private boolean executed = false;

  public final void run ()
  {
    executed = true;
  }

  public final String getNoDefaultAndNonRequired ()
  {
    return no_default_and_non_required;
  }

  public final String getNoDefaultAndRequired ()
  {
    return no_default_and_required;
  }

  public final String getDefaultAndNonRequired ()
  {
    return default_and_non_required;
  }

  public final boolean executed ()
  {
    return executed;
  }

}
