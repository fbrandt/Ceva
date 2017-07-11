package de.felixbrandt.ceva.gearman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.gearman.GearmanQueueReader;
import de.felixbrandt.ceva.gearman.MockGearmanJob;

public class GearmanQueueReaderTest
{
  List<Future<GearmanJobResult>> result_queue;
  GearmanQueueReader<Object> queue;

  @Before
  public void setUp () throws Exception
  {
    result_queue = new ArrayList<Future<GearmanJobResult>>();
    queue = new GearmanQueueReader<Object>(result_queue);
  }

  @Test
  public void testHasNextEmpty ()
  {
    assertFalse(queue.hasNext());
  }

  @Test
  public void testHasNext ()
  {
    result_queue.add(null);
    assertTrue(queue.hasNext());
  }

  @Test
  public void testGetNextEmpty ()
  {
    assertEquals(null, queue.getNext());
  }

  @Test
  public void testGetNextUndone ()
  {
    final GearmanJob result = new MockGearmanJob(null);
    final Future<GearmanJobResult> future_result = result;
    result_queue.add(future_result);

    assertEquals(null, queue.getNext());
  }

  private Future<GearmanJobResult> createDoneJob (Object result_object) throws IOException
  {
    final ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
    final ObjectOutputStream serializer = new ObjectOutputStream(out_stream);
    serializer.writeObject(result_object);

    final GearmanJobResult result = new GearmanJobResultImpl(null, true,
            out_stream.toByteArray(), new byte[0], new byte[0], 0, 0);
    return new MockGearmanJob(result);
  }

  @Test
  public void testGetNextDone () throws IOException
  {
    final Object result_object = new Integer(42);
    final Future<GearmanJobResult> future_result = createDoneJob(result_object);
    result_queue.add(future_result);

    assertEquals(result_object, queue.getNext());
    assertEquals(0, result_queue.size());
  }
}
