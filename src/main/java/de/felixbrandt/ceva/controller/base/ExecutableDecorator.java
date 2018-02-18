package de.felixbrandt.ceva.controller.base;

import java.util.Map;

public class ExecutableDecorator implements Executable
{
  private Executable wrapped_executable;
  private String replaced_run_path;
  private String replaced_version_path;
  private Integer replaced_repeat;

  public ExecutableDecorator(final Executable executable)
  {
    this(executable, null, null, null);
  }

  public ExecutableDecorator(final Executable executable, final String run_path,
          final String version_path, final Integer repeat)
  {
    wrapped_executable = executable;
    replaced_run_path = run_path;
    replaced_version_path = version_path;
    replaced_repeat = repeat;
  }

  @Override
  public String getFullVersionPath ()
  {
    if (replaced_version_path != null) {
      return replaced_version_path;
    }

    return wrapped_executable.getFullVersionPath();
  }

  @Override
  public String getFullRunPath ()
  {
    if (replaced_run_path != null) {
      return replaced_run_path;
    }

    return wrapped_executable.getFullRunPath();
  }

  @Override
  public String getName ()
  {
    return wrapped_executable.getName();
  }

  @Override
  public ResultFactory getResultFactory ()
  {
    return wrapped_executable.getResultFactory();
  }

  @Override
  public ContentMode getInputMode ()
  {
    return wrapped_executable.getInputMode();
  }

  @Override
  public int getRepeat ()
  {
    if (replaced_repeat != null) {
      return replaced_repeat;
    }

    return wrapped_executable.getRepeat();
  }

  @Override
  public Map<String, String> getParameters ()
  {
    return wrapped_executable.getParameters();
  }

  @Override
  public String getParametersAsString ()
  {
    return wrapped_executable.getParametersAsString();
  }

}
