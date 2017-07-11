package de.felixbrandt.ceva.algorithm;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.support.StreamSupport;

/**
 * Create Solution from CommandResult.
 */
public class SolutionFactory implements ResultFactory
{
  private AlgorithmExecutable executable;
  private Instance instance;
  private int version;

  public SolutionFactory(final AlgorithmExecutable _executable)
  {
    executable = _executable;
  }

  public final AlgorithmExecutable getAlgorithmExecutable ()
  {
    return executable;
  }

  public final Object create (final Command process)
  {
    final Solution result = new Solution();

    result.setAlgorithm(executable.getAlgorithm());
    result.setInstance(instance);
    result.setVersion(version);
    result.setStdout(StreamSupport.getStringFromInputStream(process.getStdout()));
    result.setStderr(StreamSupport.getStringFromInputStream(process.getStderr()));
    result.setRuntime(process.getRuntime());
    result.setParameters(executable.getParametersAsString());

    try {
      result.setMachine(InetAddress.getLocalHost().getHostName());
    } catch (final UnknownHostException e) {
      result.setMachine("unknown");
    }

    return result;
  }

  public final void setSource (final DataSource source)
  {
    assert source instanceof InstanceSource;
    instance = ((InstanceSource) source).getInstance();
  }

  public final void setVersion (final int _version)
  {
    version = _version;
  }

}
