package de.felixbrandt.ceva.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.storage.base.Storage;

/**
 * Send result objects from queue and into storage.
 */
public class QueueResultCollector implements Runnable
{
  private static final Logger LOGGER = LogManager.getLogger();
  private Storage storage;
  private QueueReader<Object> queue;

  public QueueResultCollector(final Storage _storage, final QueueReader<Object> _queue)
  {
    storage = _storage;
    queue = _queue;
  }

  public final void collect ()
  {
    LOGGER.info("starting result collection");
    int processed_results = 0;

    while (queue.hasNext()) {
      LOGGER.debug("waiting for next result");
      final Object result = queue.getNext();
      if (result != null) {
        processed_results++;
        LOGGER.info("{}: result of class {} found", processed_results, result.getClass());
        storage.add(result);
      }
    }
    LOGGER.info("finished collecting results");
  }

  public final void run ()
  {
    collect();
  }
}
