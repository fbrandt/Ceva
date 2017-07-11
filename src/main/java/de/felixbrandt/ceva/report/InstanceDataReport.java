package de.felixbrandt.ceva.report;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.provider.DataProvider;
import de.felixbrandt.ceva.provider.InstanceProvider;
import de.felixbrandt.ceva.provider.MetricProvider;

/**
 * Retrieve instance metric data.
 */
public class InstanceDataReport extends DataReport
{
  @Parameter(names = { "-i",
      "-instance" }, required = false, description = "Keyword for the instance.")
  private String instance_keyword = "*";

  private InstanceProvider instance_provider;

  public InstanceDataReport(final PrintStream _out, final InstanceProvider _instance_provider,
          final MetricProvider _metric_provider, final DataProvider _data_provider)
  {
    super(_out, _metric_provider, _data_provider);
    instance_provider = _instance_provider;
  }

  @Override
  public final void printHeader (final PrintStream out)
  {
    out.printf("%s;%s;%s;%s%n", "ID", "INSTANCE", "METRIC", "VALUE");
  }

  @Override
  public final void printRow (final PrintStream out, final Data<?, ?> data)
  {
    final Instance instance = (Instance) data.getSource();
    final Metric metric = (Metric) data.getRule();

    out.printf("%d;%s;%s;%s%n", instance.getInstance(), instance.getName(), metric.getName(),
            data.getValue().toString());
  }

  @Override
  public final Collection<?> retrieveSources ()
  {
    return instance_provider.findByKeyword(instance_keyword);
  }

  @Override
  public final Collection<?> retrieveSources (final List<String> args, final int current_index)
  {
    String keyword_from_arguments = "*";
    if (current_index < args.size()) {
      keyword_from_arguments = args.get(current_index);
    }

    return instance_provider.findByKeyword(keyword_from_arguments);
  }

  public final String getInstanceKeyword ()
  {
    return instance_keyword;
  }

}
