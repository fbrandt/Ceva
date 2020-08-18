package de.felixbrandt.ceva.init;

import de.felixbrandt.ceva.TestSessionBuilder;
import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.InstanceMetric;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.ceva.entity.SolutionMetric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SolutionViewServiceTest
{
    SessionHandler session_handler;
    SolutionViewService service;

    @Before
    public void setup ()
    {
        session_handler = TestSessionBuilder.build();
        service = new SolutionViewService(session_handler);
    }

    @After
    public void tearDown () throws Exception
    {
        session_handler.rollback();
    }

    @Test
    public void testCreateStatement ()
    {
        List<Metric> metrics = new ArrayList<Metric>();
        metrics.add(new InstanceMetric("imetric_name"));
        metrics.add(new SolutionMetric("smetric_name"));

        final String create_query = service.buildCreateStatement(metrics);
        System.out.println(create_query);
    }

    @Test
    public void testJoinInstanceMetric ()
    {
        InstanceMetric metric = new InstanceMetric("metricname");
        metric.setId(42);

        String join_query = service.buildJoin(metric);
        checkStringPattern(join_query, "LEFT OUTER JOIN(.*)");
        checkStringPattern(join_query, "(.*)InstanceDataInteger i42(.*)");
        checkStringPattern(join_query, "(.*)ON(.*)");
        checkStringPattern(join_query, "(.*)i42.source = s.instance(.*)");
        checkStringPattern(join_query, "(.*)AND(.*)");
        checkStringPattern(join_query, "(.*)i42.rule = 42(.*)");
        checkStringPattern(join_query, "(.*)i42.version = (.*)");
        checkStringPattern(join_query, "(.*)SELECT max(.*) FROM InstanceDataInteger WHERE rule = 42(.*)");
    }

    @Test
    public void testJoinSolutionMetric ()
    {
        SolutionMetric metric = new SolutionMetric("metricname");
        metric.setId(42);

        String join_query = service.buildJoin(metric);
        checkStringPattern(join_query, "LEFT OUTER JOIN(.*)");
        checkStringPattern(join_query, "(.*)SolutionDataInteger s42(.*)");
        checkStringPattern(join_query, "(.*)ON(.*)");
        checkStringPattern(join_query, "(.*)s42.source = s.solution(.*)");
        checkStringPattern(join_query, "(.*)AND(.*)");
        checkStringPattern(join_query, "(.*)s42.rule = 42(.*)");
        checkStringPattern(join_query, "(.*)s42.version = (.*)");
        checkStringPattern(join_query, "(.*)SELECT max(.*) FROM SolutionDataInteger WHERE rule = 42(.*)");
    }

    @Test
    public void testBuildSelectInstanceMetric ()
    {
        InstanceMetric metric = new InstanceMetric("metricname");
        metric.setId(42);

        String select_stmt = service.buildSelect(metric);
        checkStringPattern(select_stmt, "(.*)i42.value AS metricname(.*)");
    }

    private void checkStringPattern(final String actual, final String pattern)
    {
        assertTrue("PATTERN " + pattern + " IN " + actual, actual.matches(pattern));
    }

}
