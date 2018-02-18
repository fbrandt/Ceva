package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.algorithm.AlgorithmExecutable;
import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Solution;
import de.felixbrandt.ceva.entity.SolutionMetric;
import de.felixbrandt.ceva.metric.InstanceMetricExecutable;
import de.felixbrandt.ceva.metric.InstanceSource;
import de.felixbrandt.ceva.metric.SolutionMetricExecutable;
import de.felixbrandt.ceva.metric.SolutionSource;

public class JobTest
{
  Instance instance;
  Solution solution;
  InstanceSource isource;
  SolutionSource ssource;

  @Before
  public void setup () throws IOException
  {
    instance = new Instance();
    instance.setName("testinstance");
    isource = new InstanceSource(instance);

    solution = new Solution();
    solution.setInstance(instance);
    ssource = new SolutionSource(solution);
  }

  public byte[] serialize (final Object object) throws IOException
  {
    final ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
    ObjectOutputStream serializer;

    serializer = new ObjectOutputStream(out_stream);
    serializer.writeObject(object);

    return out_stream.toByteArray();
  }

  public static Object deserialize (final byte[] data)
          throws ClassNotFoundException, IOException
  {
    final ByteArrayInputStream in_stream = new ByteArrayInputStream(data);
    ObjectInputStream deserializer;
    deserializer = new ObjectInputStream(in_stream);
    return deserializer.readObject();
  }

  @Test
  public void testSerializationInstanceMetricJob ()
          throws IOException, ClassNotFoundException
  {
    final InstanceMetric imetric = new InstanceMetric();
    imetric.setName("mymetric");

    final Job original_job = new Job(new InstanceMetricExecutable(imetric),
            isource);
    final byte[] serialization = serialize(original_job);
    final Job rebuild_job = (Job) deserialize(serialization);

    assertFalse(isource == rebuild_job.getSource());
    assertEquals("metric mymetric", rebuild_job.getExecutable().toString());
    assertEquals("testinstance", rebuild_job.getSource().getName());
  }

  @Test
  public void testSerializationAlgorithmJob ()
          throws IOException, ClassNotFoundException
  {
    final Algorithm algorithm = new Algorithm();
    algorithm.setName("myalgo");

    final Job original_job = new Job(new AlgorithmExecutable(algorithm),
            isource);
    final byte[] serialization = serialize(original_job);
    final Job rebuild_job = (Job) deserialize(serialization);

    assertFalse(isource == rebuild_job.getSource());
    assertEquals("algorithm myalgo", rebuild_job.getExecutable().toString());
    assertEquals("testinstance", rebuild_job.getSource().getName());
  }

  @Test
  public void testSerializationSolutionMetricJob ()
          throws IOException, ClassNotFoundException
  {
    final SolutionMetric smetric = new SolutionMetric();
    smetric.setName("solmetric");

    final Job original_job = new Job(new SolutionMetricExecutable(smetric),
            ssource);
    final byte[] serialization = serialize(original_job);
    final Job rebuild_job = (Job) deserialize(serialization);

    assertFalse(isource == rebuild_job.getSource());
    assertEquals("metric solmetric", rebuild_job.getExecutable().toString());
    assertEquals("solution to testinstance", rebuild_job.getSource().getName());
  }
}
