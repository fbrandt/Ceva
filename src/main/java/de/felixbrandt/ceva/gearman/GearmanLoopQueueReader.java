package de.felixbrandt.ceva.gearman;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gearman.client.GearmanJobResult;

import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.support.SerializeSupport;

/**
 * Read results from queue, in the order they are finished.
 */
public class GearmanLoopQueueReader<ResultType> implements QueueReader<ResultType>
{
  private static final Logger LOGGER = LogManager.getLogger();
  private List<Future<GearmanJobResult>> result_queue;
  private static final int GEARMAN_TIMEOUT = 100;
  private static final int GEARMAN_LOOKAHEAD = 100;

  public GearmanLoopQueueReader(final List<Future<GearmanJobResult>> _result_queue)
  {
    result_queue = _result_queue;
  }

  public final boolean hasNext ()
  {
    return result_queue.size() > 0;
  }

  protected final GearmanJobResult getJobResult (final int index)
          throws InterruptedException, ExecutionException
  {
    try {
      return result_queue.get(index).get(GEARMAN_TIMEOUT, TimeUnit.MILLISECONDS);
    } catch (final TimeoutException e) {
      LOGGER.debug("result at queue index {} not present yet", index);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public final ResultType getNext ()
  {
    try {
      GearmanJobResult job_result = null;
      int current_index = 0;

      // Limit lookahead to at most n instances
      while (job_result == null
              && current_index < Math.min(GEARMAN_LOOKAHEAD, result_queue.size())) {
        LOGGER.debug("checking queue element {} of {}", current_index, result_queue.size());
        job_result = getJobResult(current_index);
        current_index += 1;
      }

      if (job_result != null) {
        LOGGER.debug("found result at index {}", current_index);

        final ResultType result_object = (ResultType) SerializeSupport
                .deserialize(job_result.getResults());
        result_queue.remove(current_index - 1);
        return result_object;
      } else {
        LOGGER.debug("no finished result found in queue");
      }
    } catch (final InterruptedException e) {
      LOGGER.error(e.getMessage());
    } catch (final ExecutionException e) {
      LOGGER.error(e.getMessage());
    }

    return null;
  }
}
