package de.felixbrandt.ceva.queue;

/**
 * Interface to track if more jobs are expected.
 */
public interface QueueStatus
{
  int getJobCount ();

  void setDone (boolean done);

  boolean isDone ();
}
