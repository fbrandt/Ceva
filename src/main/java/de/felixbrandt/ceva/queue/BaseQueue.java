package de.felixbrandt.ceva.queue;

import java.util.Queue;

/**
 * Simple queue for reading and writing
 */
public class BaseQueue<ElemType> implements QueueReader<ElemType>, QueueWriter<ElemType>
{
  private QueueReader<ElemType> reader;
  private QueueWriter<ElemType> writer;

  public BaseQueue(final Queue<ElemType> queue)
  {
    reader = new BaseQueueReader<ElemType>(queue);
    writer = new BaseQueueWriter<ElemType>(queue);
  }

  public final boolean add (final ElemType object)
  {
    return writer.add(object);
  }

  public final boolean hasNext ()
  {
    return reader.hasNext();
  }

  public final ElemType getNext ()
  {
    return reader.getNext();
  }

}
