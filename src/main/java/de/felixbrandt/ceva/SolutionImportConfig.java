package de.felixbrandt.ceva;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.beust.jcommander.Parameter;

import de.felixbrandt.support.StreamSupport;

/**
 * Bean containing the configuration state of an solution import operation.
 */
public class SolutionImportConfig
{
  private InputStream in;

  @Parameter(names = "-i", required = true, description = "Keyword of the instance")
  private String instance;

  @Parameter(names = "-a", required = true, description = "Name of the algorithm")
  private String algorithm;

  @Parameter(names = "-v", required = false, description = "Version of the algorithm")
  private String version = "";

  @Parameter(names = "-m", required = false, description = "Machine the solution was computed on")
  private String machine = "imported";

  @Parameter(names = "-t", required = false, description = "Solution runtime in seconds")
  private String runtime = "-1";

  @Parameter(names = "-p", required = false, description = "Algorithm parameters")
  private String params = "";

  @Parameter(names = "-o", required = false, description = "Solution output file")
  private String std_output;

  @Parameter(names = "-e", required = false, description = "Solution error file")
  private String std_error;

  public SolutionImportConfig(final InputStream _in)
  {
    in = _in;
  }

  public final void setStdoutFile (final String filename)
  {
    std_output = filename;
  }

  public final InputStream getStdoutStream () throws FileNotFoundException
  {
    if (std_output == null) {
      return in;
    }

    return new FileInputStream(std_output);
  }

  public final void setStderrFile (final String filename)
  {
    std_error = filename;
  }

  public final InputStream getStderrStream () throws FileNotFoundException
  {
    if (std_error == null) {
      return StreamSupport.createEmptyInputStream();
    }

    return new FileInputStream(std_error);
  }

  public final String getInstanceKeyword ()
  {
    return instance;
  }

  public final void setInstanceKeyword (final String _instance)
  {
    this.instance = _instance;
  }

  public final String getAlgorithmName ()
  {
    return algorithm;
  }

  public final void setAlgorithmName (final String _algorithm)
  {
    this.algorithm = _algorithm;
  }

  public final int getVersion ()
  {
    try {
      return Integer.parseInt(version);
    } catch (final Exception e) {
      return 0;
    }
  }

  public final void setVersion (final int _version)
  {
    this.version = Integer.toString(_version);
  }

  public final String getMachine ()
  {
    return machine;
  }

  public final void setMachine (final String _machine)
  {
    machine = _machine;
  }

  public final int getRuntime ()
  {
    try {
      return Integer.parseInt(runtime);
    } catch (final Exception e) {
      return -1;
    }
  }

  public final void setRuntime (final int _runtime)
  {
    this.runtime = Integer.toString(_runtime);
  }

  public final String getParams ()
  {
    return params;
  }

  public final void setParams (final String _params)
  {
    params = _params;
  }

}
