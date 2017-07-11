package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.provider.FixedInstanceProvider;
import de.felixbrandt.ceva.report.InstanceReport;

public class InstanceReportTest
{

  ByteArrayOutputStream stream;
  FixedInstanceProvider provider;
  InstanceReport report;

  @Before
  public void setUp () throws Exception
  {
    stream = new ByteArrayOutputStream();
    provider = new FixedInstanceProvider();
    report = new InstanceReport(new PrintStream(stream), provider);
  }

  @Test
  public void testNoResult ()
  {
    new JCommander(report, "-i foobar".split(" "));
    report.run();
    assertEquals("foobar", provider.getLastKeyword());
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)no matching instance found(.*)"));
  }

  @Test
  public void testOneResult ()
  {
    final Instance instance = new Instance();
    instance.setContent("Hello World");
    provider.add(instance);
    report.run();
    assertTrue(stream.toString(), stream.toString().matches("Hello World"));
  }

  @Test
  public void testMultipleResults ()
  {
    final Instance instance1 = new Instance();
    instance1.setInstance(42);
    instance1.setName("test.txt");
    instance1.setChecksum("ABCDEF");
    final Instance instance2 = new Instance();
    instance2.setInstance(43);
    instance2.setName("foobar.txt");
    instance2.setChecksum("123456");
    provider.add(instance1);
    provider.add(instance2);

    report.run();
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)multiple instances found:(.*)"));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)42(.*)test.txt(.*)ABCDEF(.*)"));
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)43(.*)foobar.txt(.*)123456(.*)"));
  }

  @Test
  public void testArgsIndex ()
  {
    new JCommander(report, "-i 42".split(" "));
    report.run();
    assertEquals("42", provider.getLastKeyword());
  }

  @Test
  public void testRunMissingParam ()
  {
    report.run();
    assertEquals("*", provider.getLastKeyword());
    assertTrue(stream.toString(),
            stream.toString().matches("(?s)(.*)no matching instance found(.*)"));
  }
}
