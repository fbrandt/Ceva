package de.felixbrandt.ceva.entity;

/**
 * Solution of an Instance created by an Algorithm.
 */
@SuppressWarnings("designforextension")
public class Solution implements java.io.Serializable
{
  private static final long serialVersionUID = 2L;

  private int solution;
  private Algorithm algorithm;
  private Instance instance;
  private int version;
  private String stdout;
  private String stderr;
  private String machine;
  private double runtime;
  private int exitcode;
  private String parameters;

  public Solution()
  {
    this(0, null, null, 0);
  }

  public Solution(final Instance _instance, final Algorithm _algorithm, final int _version)
  {
    this(0, _algorithm, _instance, _version);
  }

  public Solution(final int _id, final Algorithm _algorithm, final Instance _instance,
          final int _version)
  {
    solution = _id;
    algorithm = _algorithm;
    instance = _instance;
    version = _version;
    stdout = "";
    stderr = "";
    machine = "";
    runtime = -1.0;
    parameters = "";
  }

  public int getSolution ()
  {
    return solution;
  }

  public void setSolution (final int solution_id)
  {
    solution = solution_id;
  }

  public Instance getInstance ()
  {
    return instance;
  }

  public void setInstance (final Instance _instance)
  {
    instance = _instance;
  }

  public Algorithm getAlgorithm ()
  {
    return algorithm;
  }

  public void setAlgorithm (final Algorithm _algorithm)
  {
    algorithm = _algorithm;
  }

  public int getVersion ()
  {
    return version;
  }

  public void setVersion (final int _version)
  {
    version = _version;
  }

  public String getStdout ()
  {
    return stdout;
  }

  public void setStdout (final String _stdout)
  {
    this.stdout = _stdout;
  }

  public String getStderr ()
  {
    return stderr;
  }

  public void setStderr (final String _stderr)
  {
    this.stderr = _stderr;
  }

  public String getMachine ()
  {
    return machine;
  }

  public void setMachine (final String _machine)
  {
    machine = _machine;
  }

  public double getRuntime ()
  {
    return runtime;
  }

  public void setRuntime (final double _runtime)
  {
    runtime = _runtime;
  }

  public int getExitcode ()
  {
    return exitcode;
  }

  public void setExitcode (final int _exitcode)
  {
    exitcode = _exitcode;
  }

  public String getParameters ()
  {

    // Needed for old database entries that contain 'null' as parameters.
    if (parameters == null) {
      return "";
    }

    return parameters;
  }

  public void setParameters (final String params)
  {
    parameters = params;
  }
}
