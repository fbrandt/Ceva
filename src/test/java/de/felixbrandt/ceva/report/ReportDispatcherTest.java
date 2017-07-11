package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.report.ReportDispatcher;
import de.felixbrandt.ceva.report.ReportService;

public class ReportDispatcherTest
{
  ReportDispatcher dispatcher;

  class MockReport implements ReportService
  {
    public List<String> last_args;
    public int last_index;

    public void run (final List<String> args, final int current_index)
    {
      last_args = args;
      last_index = current_index;
    }
  }

  @Before
  public void setUp () throws Exception
  {
    dispatcher = new ReportDispatcher();
  }

  @Test
  public void testBrokenInput ()
  {
    // just check we do not crash
    dispatcher.run(null, 0);
    dispatcher.run(new ArrayList<String>(), 3);
  }

  @Test
  public void testAddReport ()
  {
    final MockReport report = new MockReport();
    dispatcher.addReport("name", report);
    final Map<String, ReportService> reports = dispatcher.getReports();
    assertEquals(1, reports.size());
    assertTrue(reports.containsKey("name"));
    assertEquals(report, reports.get("name"));
  }

  @Test
  public void testConstructor ()
  {
    final Map<String, ReportService> reports = new HashMap<String, ReportService>();
    reports.put("name", new MockReport());
    dispatcher = new ReportDispatcher(reports);
    assertEquals(reports, dispatcher.getReports());
  }

  @Test
  public void testRun ()
  {
    final MockReport report = new MockReport();
    dispatcher.addReport("name", report);
    final List<String> args = new ArrayList<String>();
    args.add("programm.exe");
    args.add("name");
    dispatcher.run(args, 1);
    assertEquals(args, report.last_args);
    assertEquals(2, report.last_index);
  }
}
