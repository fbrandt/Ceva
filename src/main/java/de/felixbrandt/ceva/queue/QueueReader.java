package de.felixbrandt.ceva.queue;

/**
 * Interface to read elements from a queue.
 */
public interface QueueReader<ElemType>
{
  boolean hasNext ();

  ElemType getNext ();
}
