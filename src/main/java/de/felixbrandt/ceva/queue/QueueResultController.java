package de.felixbrandt.ceva.queue;

import de.felixbrandt.ceva.controller.base.Controller;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Send results into queue.
 */
public class QueueResultController implements Controller
{
  private final Controller sub_controller;
  private final QueueWriter<Object> result_queue;

  public QueueResultController(final Controller _sub_controller,
          final QueueWriter<Object> _result_queue)
  {
    sub_controller = _sub_controller;
    result_queue = _result_queue;
  }

  public final Object run (final Executable executable, final DataSource source)
  {
    final Object result = sub_controller.run(executable, source);
    result_queue.add(result);

    return result;
  }

}
