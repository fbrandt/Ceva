package de.felixbrandt.ceva.report;

/**
 * Creates reports based on already instantiated parameters.
 */
public interface ConfigurableReportService<Config>
{
  void run (Config config);
}
