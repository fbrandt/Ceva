package de.felixbrandt.ceva.queue;

/**
 * Count jobs submitted to queue
 */
public class QueueWriterStatus<ElemType> implements QueueWriter<ElemType>, QueueStatus
{
  private QueueWriter<ElemType> queue;
  private int count;
  private boolean done;

  public QueueWriterStatus(final QueueWriter<ElemType> base_queue)
  {
    queue = base_queue;
    count = 0;
    done = false;
  }

  public final void add (final ElemType object)
  {
    queue.add(object);
    count++;
  }

  public final int getJobCount ()
  {
    return count;
  }

  public final void setDone (final boolean done_state)
  {
    done = done_state;
  }

  public final boolean isDone ()
  {
    return done;
  }

}
