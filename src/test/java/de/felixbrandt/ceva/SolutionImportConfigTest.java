package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.felixbrandt.ceva.SolutionImportConfig;
import de.felixbrandt.support.StreamSupport;

public class SolutionImportConfigTest
{
  SolutionImportConfig config;
  InputStream in;

  @Before
  public void setUp () throws Exception
  {
    in = StreamSupport.createEmptyInputStream();
    config = new SolutionImportConfig(in);
  }

  @Test
  public void testInputStream () throws FileNotFoundException
  {
    assertEquals(in, config.getStdoutStream());
  }

  @Test
  public void testInputFile () throws FileNotFoundException
  {
    new JCommander(config, "-i I -a A -o test/stdout_sample.txt".split(" "));
    InputStream stream = config.getStdoutStream();
    assertTrue(stream instanceof FileInputStream);
    assertEquals("output", StreamSupport.getStringFromInputStream(stream));
  }

  @Test
  public void testErrorDefault () throws FileNotFoundException
  {
    InputStream stream = config.getStderrStream();
    assertTrue(stream instanceof ByteArrayInputStream);
    assertEquals("", StreamSupport.getStringFromInputStream(stream));
  }

  @Test
  public void testErrorFile () throws FileNotFoundException
  {
    new JCommander(config, "-i I -a A -e test/stderr_sample.txt".split(" "));
    InputStream stream = config.getStderrStream();
    assertTrue(stream instanceof FileInputStream);
    assertEquals("errors", StreamSupport.getStringFromInputStream(stream));
  }

  @Test(expected = ParameterException.class)
  public void testRequiredParams ()
  {
    new JCommander(config, "".split(" "));
  }

  @Test(expected = ParameterException.class)
  public void testRequiredAlgorithmParam ()
  {
    new JCommander(config, "-i instance".split(" "));
  }

  @Test
  public void testDefaults ()
  {
    assertEquals(null, config.getInstanceKeyword());
    assertEquals(null, config.getAlgorithmName());
    assertEquals(0, config.getVersion());
    assertEquals("imported", config.getMachine());
    assertEquals(-1, config.getRuntime());
    assertEquals("", config.getParams());
  }

  @Test
  public void testParams ()
  {
    new JCommander(config,
            "-i instance -a algorithm -v 23 -m machine -t 42 -p PARAMS".split(" "));
    assertEquals("instance", config.getInstanceKeyword());
    assertEquals("algorithm", config.getAlgorithmName());
    assertEquals(23, config.getVersion());
    assertEquals("machine", config.getMachine());
    assertEquals(42, config.getRuntime());
    assertEquals("PARAMS", config.getParams());
  }

  @Test
  public void testResolveVersionNoInteger ()
  {
    new JCommander(config, "-i instance -a algoname -v algoname".split(" "));
    assertEquals(0, config.getVersion());
  }

  @Test
  public void testResolveRuntimeNoInteger ()
  {
    new JCommander(config, "-i instance -a algoname -t FOUR".split(" "));
    assertEquals(-1, config.getRuntime());
  }

}
