package de.felixbrandt.ceva.gearman;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueWriter;

/**
 * A queue with a blocking add (waiting for the retrieval of the object by another thread). The
 * getNext() is non-blocking and might return null.
 */
public class SynchedQueue<ElemType> implements QueueReader<ElemType>, QueueWriter<ElemType>
{
  private static final Logger LOGGER = LogManager.getLogger();
  private static final long WAIT_TIME = 100;
  private ElemType queued_element;

  public final boolean add (final ElemType object)
  {
    while (queued_element != null) {
      try {
        Thread.sleep(WAIT_TIME);
      } catch (final InterruptedException e) {
        LOGGER.warn("interrupted waiting for last element to be removed");
      }
    }

    replace(object);

    while (queued_element != null) {
      try {
        Thread.sleep(WAIT_TIME);
      } catch (final InterruptedException e) {
        LOGGER.warn("interrupted waiting for current element to be removed");
      }
    }

    return true;
  }

  private synchronized ElemType replace (final ElemType value)
  {
    final ElemType old = queued_element;
    queued_element = value;

    return old;
  }

  public final boolean hasNext ()
  {
    return queued_element != null;
  }

  public final ElemType getNext ()
  {
    if (queued_element == null) {
      return null;
    }

    return replace(null);
  }
}
