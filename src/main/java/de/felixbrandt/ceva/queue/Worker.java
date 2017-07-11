package de.felixbrandt.ceva.queue;

/**
 * An asynchronuous CEVA worker thread
 */
public interface Worker extends Runnable
{
  void stop ();
}
