package de.felixbrandt.ceva.queue;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.controller.MockController;
import de.felixbrandt.ceva.controller.MockDataSource;
import de.felixbrandt.ceva.controller.MockExecutable;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.Executable;
import de.felixbrandt.ceva.queue.BaseQueueReader;
import de.felixbrandt.ceva.queue.Job;
import de.felixbrandt.ceva.queue.QueueJobWorker;

public class QueueJobWorkerTest
{
  Executable executable;
  DataSource datasource;
  Job job;
  LinkedList<Job> queue;
  MockController controller;
  QueueJobWorker worker;

  @Before
  public void setUp () throws Exception
  {
    executable = new MockExecutable();
    datasource = new MockDataSource();
    job = new Job(executable, datasource);
    queue = new LinkedList<Job>();
    queue.add(job);
    controller = new MockController();
    worker = new QueueJobWorker(new BaseQueueReader<Job>(queue), controller);
  }

  @Test(timeout = 2000)
  public void testRun () throws InterruptedException
  {
    final Thread worker_thread = new Thread(worker);
    worker_thread.start();

    Thread.sleep(50);
    worker.stop();
    worker_thread.join();

    assertEquals(0, queue.size());
    assertEquals(executable, controller.getLastRunExecutable());
    assertEquals(datasource, controller.getLastSource());
    assertEquals(1, controller.getCallCount());
  }
}
