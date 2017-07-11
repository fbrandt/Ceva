package de.felixbrandt.ceva.report;

import java.io.PrintStream;
import java.util.Collection;

import com.beust.jcommander.Parameter;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.provider.InstanceProvider;

/**
 * Retrieve instance content.
 */
public class InstanceReport implements GenericReportService
{
  private PrintStream out;
  private InstanceProvider provider;

  @Parameter(names = { "-i",
      "-instance" }, required = false, description = "Keyword for the instance.")
  private String instance_keyword = "*";

  public InstanceReport(final PrintStream _out, final InstanceProvider _provider)
  {
    out = _out;
    provider = _provider;
  }

  /**
   * Print list of instances or instance content (if exact match found).
   */
  public final void run ()
  {
    run(instance_keyword);
  }

  public final void run (final String _keyword)
  {
    final Collection<Instance> result = provider.findByKeyword(_keyword);

    if (result.size() == 0) {
      out.println("ERROR: no matching instance found");
    } else if (result.size() == 1) {
      out.print(result.iterator().next().getContent());
    } else {
      out.println("ERROR: multiple instances found: (select one by id or name)");
      for (final Instance i : result) {
        out.printf("%1$8d %2$-16s %3$-32s%n", i.getInstance(), i.getName(), i.getChecksum());
      }
    }
  }

  public final String getInstanceKeyword ()
  {
    return instance_keyword;
  }
}
