package de.felixbrandt.ceva.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection of reports, select one by current argument.
 */
public class ReportDispatcher implements ReportService
{
  private Map<String, ReportService> reports;

  public ReportDispatcher()
  {
    this(new HashMap<String, ReportService>());
  }

  public ReportDispatcher(final Map<String, ReportService> _reports)
  {
    reports = _reports;
  }

  public final void run (final List<String> args, final int current_index)
  {
    if (args != null && current_index < args.size()) {
      final ReportService service = reports.get(args.get(current_index));
      if (service != null) {
        service.run(args, current_index + 1);
      }
    }
  }

  public final void addReport (final String string, final ReportService report)
  {
    reports.put(string, report);
  }

  public final Map<String, ReportService> getReports ()
  {
    return reports;
  }

}
