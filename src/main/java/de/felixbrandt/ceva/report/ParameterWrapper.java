package de.felixbrandt.ceva.report;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;

/**
 * Wraps generic report services and instantiates its value
 * when run.
 */
public class ParameterWrapper implements ReportService
{

  private GenericReportService service;

  public ParameterWrapper(final GenericReportService _service)
  {
    service = _service;
  }

  public final void run (final List<String> args, final int current_index)
  {
    new JCommander(service, Arrays.copyOfRange(args.toArray(new String[args.size()]),
            current_index, args.size()));
    service.run();
  }

}
