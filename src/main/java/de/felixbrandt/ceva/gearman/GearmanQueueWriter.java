package de.felixbrandt.ceva.gearman;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobImpl;
import org.gearman.client.GearmanJobResult;

import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.support.SerializeSupport;

/**
 * Send objects to a gearman queue
 */
public class GearmanQueueWriter<ElemType> implements QueueWriter<ElemType>
{
  private static final Logger LOGGER = LogManager.getLogger();
  private GearmanClient client;
  private String queue_name;
  private List<Future<GearmanJobResult>> result_queue;

  public GearmanQueueWriter(final GearmanClient _client, final String _queue_name,
          final List<Future<GearmanJobResult>> _result_queue)
  {
    client = _client;
    queue_name = _queue_name;
    result_queue = _result_queue;
  }

  public final boolean add (final ElemType object)
  {
    final byte[] data = SerializeSupport.serialize(object);
    final GearmanJob job = GearmanJobImpl.createJob(queue_name, data, null);

    Future<GearmanJobResult> result = null;
    try {
      result = client.submit(job);

      if (result_queue != null && result != null) {
        result_queue.add(result);
      }

      LOGGER.debug("submitted job to queue {}", queue_name);
      return true;
    } catch (final RejectedExecutionException e) {
      LOGGER.error("failed to execute job via queue " + queue_name);
      return false;
    }
  }

}
