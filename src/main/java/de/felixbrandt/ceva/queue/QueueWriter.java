package de.felixbrandt.ceva.queue;

/**
 * Interface to send elements to a queue
 */
public interface QueueWriter<ElemType>
{
  boolean add (ElemType object);
}
