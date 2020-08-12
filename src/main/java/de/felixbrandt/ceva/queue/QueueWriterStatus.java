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

  public final boolean add (final ElemType object)
  {
    if (done) {
      return false;
    }

    queue.add(object);
    count++;

    return true;
  }

  public final int getJobCount ()
  {
    return count;
  }

  public final void setDone (final boolean done_state)
  {
    done = done_state;
  }

  /**
   * No more jobs will be added to queue.
   */
  public final boolean isDone ()
  {
    return done;
  }

  /**
   * Not more than the given job count was pushed to this queue.
   */
  public final boolean isDone (final int actual_count)
  {
    return isDone() && actual_count >= count;
  }
}
