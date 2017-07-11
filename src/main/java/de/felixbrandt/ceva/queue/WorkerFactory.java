package de.felixbrandt.ceva.queue;

/**
 * Abstract factory for creating Worker instances.
 */
public interface WorkerFactory
{
  Worker create ();
}
