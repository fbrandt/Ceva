package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import de.felixbrandt.ceva.QueueExecutionStrategy;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.provider.DataSourceMemoryProvider;
import de.felixbrandt.ceva.provider.ExecutableMemoryProvider;
import de.felixbrandt.ceva.queue.BaseQueueReader;
import de.felixbrandt.ceva.queue.BaseQueueWriter;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueReader;
import de.felixbrandt.ceva.queue.QueueWriter;
import de.felixbrandt.ceva.storage.MockStorage;

public class QueueExecutionStrategyTest
{
  @Test(timeout = 1000)
  public void test ()
  {
    final Queue<Job> jobs = new LinkedList<Job>();
    final QueueWriter<Job> job_queue = new BaseQueueWriter<Job>(jobs);

    final Queue<Object> results = new LinkedList<Object>();
    for (int i = 0; i < 4; i++) {
      results.add(new Object());
    }
    final QueueReader<Object> result_queue = new BaseQueueReader<Object>(results);

    final QueueExecutionStrategy strategy = new QueueExecutionStrategy(job_queue, result_queue);
    final MockStorage storage = new MockStorage();
    final ExecutableMemoryProvider executables = new ExecutableMemoryProvider();
    final MockExecutable executable = new MockExecutable();
    executable.setRunPath("echo 1");

    executables.add(executable);
    executables.add(executable);

    final DataSourceMemoryProvider sources = new DataSourceMemoryProvider();
    sources.add(new MockDataSource());
    sources.add(new MockDataSource());

    strategy.run(executables, sources, storage, null);
    assertEquals(4, jobs.size());
    assertEquals(4, storage.getAddCount());
  }

}
