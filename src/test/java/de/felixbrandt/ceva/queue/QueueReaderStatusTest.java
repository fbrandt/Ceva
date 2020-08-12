package de.felixbrandt.ceva.queue;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class QueueReaderStatusTest
{
    /**
     * BUG: QueueReaderStatus::hasNext() returns false if result_queue is empty
     *      although there are still open jobs in the given job_queue
     */
    @Test
    public void testHasNext ()
    {
        final Queue<Job> jobs = new LinkedList<Job>();
        final QueueWriter<Job> job_queue = new BaseQueueWriter<Job>(jobs);
        final Queue<Object> results = new LinkedList<Object>();
        final QueueReader<Object> result_queue = new BaseQueueReader<Object>(results);

        final QueueWriterStatus<Job> job_queue_status = new QueueWriterStatus<Job>(job_queue);
        final QueueReaderStatus<Object> result_queue_status = new QueueReaderStatus<Object>(job_queue_status, result_queue);

        job_queue_status.add(new Job(null, null));
        job_queue_status.setDone(true);

        assertTrue(result_queue_status.hasNext());

        results.add(new Object());
        result_queue_status.getNext();
        assertFalse(result_queue_status.hasNext());
    }

}
