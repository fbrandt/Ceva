package de.felixbrandt.ceva.queue;

import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Execution job to send via QueueWriter
 */
public class Job implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;

  private Executable executable;
  private DataSource source;

  public Job(final Executable _executable, final DataSource _source)
  {
    executable = _executable;
    source = _source;
  }

  public final Executable getExecutable ()
  {
    return executable;
  }

  public final DataSource getSource ()
  {
    return source;
  }
}
