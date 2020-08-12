package de.felixbrandt.ceva.queue;

import java.util.Queue;

/**
 * Write objects into a java queue
 */
public class BaseQueueWriter<ElemType> implements QueueWriter<ElemType>
{
  private Queue<ElemType> queue;

  public BaseQueueWriter(final Queue<ElemType> _queue)
  {
    queue = _queue;
  }

  public final boolean add (final ElemType object)
  {
    return queue.add(object);
  }

}
