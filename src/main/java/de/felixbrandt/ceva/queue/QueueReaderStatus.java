package de.felixbrandt.ceva.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Track status of read queue (are more elements to expect?)
 */
public class QueueReaderStatus<ElemType> implements QueueReader<ElemType>
{
  private static final Logger LOGGER = LogManager.getLogger();
  private static final int WAIT_INTERVAL = 100;
  private QueueStatus status;
  private QueueReader<ElemType> result_queue;
  private int result_count;

  public QueueReaderStatus(final QueueStatus _status,
          final QueueReader<ElemType> _result_queue)
  {
    status = _status;
    result_queue = _result_queue;
    result_count = 0;
  }

  public final boolean hasNext ()
  {
    return !status.isDone(result_count) || result_queue.hasNext();
  }

  @SuppressWarnings("unchecked")
  public final ElemType getNext ()
  {
    while (this.hasNext()) {
      LOGGER.debug("retrieving next result");
      final Object result = result_queue.getNext();
      if (result != null) {
        result_count++;
        LOGGER.debug("returning result {}", result_count);
        return (ElemType) result;
      } else {
        LOGGER.debug("no result found, waiting for {} ms", WAIT_INTERVAL);
        try {
          Thread.sleep(WAIT_INTERVAL);
        } catch (final InterruptedException e) {
          LOGGER.warn("interrupted waiting for next result");
        }
      }
    }

    LOGGER.warn("no next result present");
    return null;
  }

}
