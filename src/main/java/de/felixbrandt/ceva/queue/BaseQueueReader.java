package de.felixbrandt.ceva.queue;

import java.util.Queue;

/**
 * Read objects from a java queue.
 */
public class BaseQueueReader<ElemType> implements QueueReader<ElemType>
{
  private Queue<ElemType> queue;

  public BaseQueueReader(final Queue<ElemType> _queue)
  {
    queue = _queue;
  }

  public final boolean hasNext ()
  {
    return queue.size() > 0;
  }

  public final ElemType getNext ()
  {
    return queue.poll();
  }

}
