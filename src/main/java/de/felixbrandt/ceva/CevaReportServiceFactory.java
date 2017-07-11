package de.felixbrandt.ceva;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.provider.AlgorithmDBProvider;
import de.felixbrandt.ceva.provider.AlgorithmProvider;
import de.felixbrandt.ceva.provider.DataProvider;
import de.felixbrandt.ceva.provider.InstanceDBProvider;
import de.felixbrandt.ceva.provider.InstanceMetricDBProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.provider.MetricProvider;
import de.felixbrandt.ceva.provider.SolutionDBProvider;
import de.felixbrandt.ceva.provider.SolutionMetricDBProvider;
import de.felixbrandt.ceva.provider.SolutionProvider;
import de.felixbrandt.ceva.report.ConfigurableReportParameterWrapper;
import de.felixbrandt.ceva.report.InstanceDataReport;
import de.felixbrandt.ceva.report.InstanceReport;
import de.felixbrandt.ceva.report.ParameterWrapper;
import de.felixbrandt.ceva.report.ReportDispatcher;
import de.felixbrandt.ceva.report.ReportService;
import de.felixbrandt.ceva.report.SolutionDataReport;
import de.felixbrandt.ceva.report.SolutionReport;
import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Setup the CEVA report service.
 */
public final class CevaReportServiceFactory
{
  /** Class cannot be instantiated */
  private CevaReportServiceFactory()
  {
  }

  public static ReportService setup (final SessionHandler session_handler,
          final InputStream in, final PrintStream out)
  {
    final HashMap<String, ReportService> reports = new HashMap<String, ReportService>();

    final InstanceProvider instance_provider = new InstanceDBProvider(session_handler);
    reports.put("instance", new ParameterWrapper(new InstanceReport(out, instance_provider)));

    final AlgorithmProvider algo_provider = new AlgorithmDBProvider(session_handler);
    final Storage solution_storage = new SolutionDBStorage(session_handler);
    reports.put("import", new ConfigurableReportParameterWrapper<SolutionImportConfig>(
            new SolutionImportService(out, instance_provider, algo_provider, solution_storage),
            new SolutionImportConfig(in)));

    final MetricProvider instance_metric_provider = new InstanceMetricDBProvider(
            session_handler);
    final DataProvider instance_data_provider = new InstanceDataDBStorage(session_handler);
    reports.put("imetrics", new ParameterWrapper(new InstanceDataReport(out, instance_provider,
            instance_metric_provider, instance_data_provider)));

    final SolutionProvider solution_provider = new SolutionDBProvider(session_handler);
    reports.put("solution", new ParameterWrapper(
            new SolutionReport(out, instance_provider, algo_provider, solution_provider)));

    final MetricProvider solution_metric_provider = new SolutionMetricDBProvider(
            session_handler);
    final DataProvider solution_data_provider = new SolutionDataDBStorage(session_handler);
    reports.put("smetrics",
            new ParameterWrapper(new SolutionDataReport(out, instance_provider, algo_provider,
                    solution_provider, solution_metric_provider, solution_data_provider)));

    return new ReportDispatcher(reports);
  }
}
