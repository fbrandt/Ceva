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
 * Read results from queue, in original order.
 */
public class GearmanQueueReader<ResultType> implements QueueReader<ResultType>
{
  private static final Logger LOGGER = LogManager.getLogger();
  private List<Future<GearmanJobResult>> result_queue;

  public GearmanQueueReader(final List<Future<GearmanJobResult>> _result_queue)
  {
    result_queue = _result_queue;
  }

  public final boolean hasNext ()
  {
    return result_queue.size() > 0;
  }

  @SuppressWarnings("unchecked")
  public final ResultType getNext ()
  {
    if (!result_queue.isEmpty()) {
      try {
        final GearmanJobResult job_result = result_queue.get(0).get(100,
                TimeUnit.MILLISECONDS);
        if (job_result != null) {
          final ResultType result_object = (ResultType) SerializeSupport
                  .deserialize(job_result.getResults());
          result_queue.remove(0);
          return result_object;
        }
      } catch (final InterruptedException e) {
        LOGGER.error(e.getMessage());
      } catch (final ExecutionException e) {
        LOGGER.error(e.getMessage());
      } catch (final TimeoutException e) {
        LOGGER.debug("result not present yet");
      }
    }

    return null;
  }

}
