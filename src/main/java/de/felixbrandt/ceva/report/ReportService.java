package de.felixbrandt.ceva.report;

import java.util.List;

/**
 * Create reports based on the given parameters.
 */
public interface ReportService
{
  void run (final List<String> args, int current_index);
}

