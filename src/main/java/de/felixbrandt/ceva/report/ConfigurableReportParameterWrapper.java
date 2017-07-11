package de.felixbrandt.ceva.report;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.JCommander;

/**
 * Wraps generic report services and instantiates its value when run.
 */
public class ConfigurableReportParameterWrapper<Config> implements ReportService
{
  private Config base_config;
  private ConfigurableReportService<Config> service;

  public ConfigurableReportParameterWrapper(final ConfigurableReportService<Config> _service,
          final Config _base_config)
  {
    service = _service;
    base_config = _base_config;
  }

  public final void run (final List<String> args, final int current_index)
  {
    new JCommander(base_config, Arrays.copyOfRange(args.toArray(new String[args.size()]),
            current_index, args.size()));
    service.run(base_config);
  }

}
