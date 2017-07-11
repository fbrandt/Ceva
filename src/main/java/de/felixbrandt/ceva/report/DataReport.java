package de.felixbrandt.ceva.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.Rule;
import de.felixbrandt.ceva.provider.DataProvider;
import de.felixbrandt.ceva.provider.MetricProvider;

/**
 * Base class for CSV export of metric data.
 */
public abstract class DataReport implements GenericReportService, ReportService
{
  private final PrintStream output;
  private final MetricProvider metric_provider;
  private final DataProvider data_provider;

  private static final int METRIC_OFFSET = 1;

  @Parameter(names = "-m", required = false, description = "Name of the metric.")
  private String metric_name = "all";

  public DataReport(final PrintStream _output, final MetricProvider _metric_provider,
          final DataProvider _data_provider)
  {
    output = _output;
    metric_provider = _metric_provider;
    data_provider = _data_provider;
  }

  protected final PrintStream getStream ()
  {
    return output;
  }

  public final void run ()
  {
    final Collection<?> sources = retrieveSources();
    final Collection<? extends Metric> metrics = retrieveMetrics();

    if (sources != null && metrics != null) {
      final Collection<Data<?, ?>> data = retrieveData(sources, metrics);
      print(data);
    }
  }

  public final void run (final List<String> args, final int current_index)
  {
    final Collection<?> sources = retrieveSources(args, current_index);
    final Collection<? extends Metric> metrics = retrieveMetrics(args, current_index);

    if (sources != null && metrics != null) {
      final Collection<Data<?, ?>> data = retrieveData(sources, metrics);
      print(data);
    }
  }

  public final void print (final Collection<Data<?, ?>> list)
  {
    if (list != null) {
      printHeader(output);

      for (final Data<?, ?> data : list) {
        printRow(output, data);
      }
    }
  }

  public abstract void printHeader (PrintStream out);

  public abstract void printRow (PrintStream out, final Data<?, ?> data);

  public abstract Collection<?> retrieveSources ();

  public final Collection<? extends Metric> retrieveMetrics ()
  {
    return metric_provider.findByName(metric_name);
  }

  public abstract Collection<?> retrieveSources (final List<String> args,
          final int current_index);

  public final Collection<? extends Metric> retrieveMetrics (final List<String> args,
          final int current_index)
  {
    final int metric_index = current_index + METRIC_OFFSET;
    String name = "all";
    if (metric_index < args.size()) {
      name = args.get(metric_index);
    }

    return metric_provider.findByName(name);
  }

  public final Collection<Data<?, ?>> retrieveData (final Collection<?> sources,
          final Collection<? extends Metric> metrics)
  {
    final Collection<Data<?, ?>> result = new ArrayList<Data<?, ?>>();

    for (final Rule metric : metrics) {
      result.addAll(data_provider.getData(metric, sources));
    }

    return result;
  }

  public final String getMetricName ()
  {
    return metric_name;
  }

}
