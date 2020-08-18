package de.felixbrandt.ceva.init;

import de.felixbrandt.ceva.database.SessionHandler;
import de.felixbrandt.ceva.entity.Metric;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class SolutionViewService
{
    private static final Logger LOGGER = LogManager.getLogger();
    private SessionHandler session_handler;
    private static String view_name = "solution_view";

    public SolutionViewService(final SessionHandler _session_handler)
    {
        this.session_handler = _session_handler;
    }

    public final void updateView (final Collection<Metric> metrics)
    {
        final Session session = session_handler.getSession();

        try {
            dropView(session);
            createView(session, metrics);
        } catch(Exception e) {
            LOGGER.error("Failed to update solution query: " + e.getMessage());
        }
    }

    public final void dropView (final Session session)
    {
        session.createSQLQuery("DROP VIEW IF EXISTS " + view_name + ";").executeUpdate();
    }

    public final void createView (final Session session, final Collection<Metric> metrics)
    {
        String create_query = buildCreateStatement(metrics);
        session.doWork(new Work(){
            @Override
            public void execute(Connection c) throws SQLException {
                c.prepareCall(create_query).execute();
            }
        });
    }

    public final String buildCreateStatement (final Collection<Metric> metrics)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE VIEW " + view_name + " AS ");
        builder.append("SELECT solution, i.name AS instance, a.name AS algorithm, s.version, s.machine, s.runtime, s.exitcode");

        for(final Metric m: metrics) {
            builder.append(", " + buildSelect(m));
        }

        builder.append(" FROM solution s JOIN instance i USING (instance) JOIN algorithm a ON a.id=s.algorithm");

        for(final Metric m: metrics) {
            builder.append(" " + buildJoin(m));
        }

        builder.append(";");

        return builder.toString();
    }

    public final String buildSelect (final Metric metric)
    {
        String join_id = getJoinID(metric);
        return join_id + ".value AS " + metric.getName().toLowerCase();
    }

    public final String buildJoin (final Metric metric)
    {
        String join_id = getJoinID(metric);
        return "LEFT OUTER JOIN " +
                metric.getDataEntity() + " " + join_id +
                " ON " +
                join_id + ".source = s." + metric.getSourceReference() +
                " AND " +
                join_id + ".rule = " + metric.getId() +
                " AND " +
                join_id + ".version = (SELECT max(version) FROM " + metric.getDataEntity() + " WHERE rule = " + metric.getId() + ")";
    }

    public final String getJoinID (final Metric metric)
    {
        return metric.getDataEntity().substring(0, 1).toLowerCase() + metric.getId();
    }
}
