package de.felixbrandt.ceva.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Generic SessionHandler proving persistence. Hides all Hibernate specific configuration.
 */
public class SessionHandler
{
  private Configuration configuration;
  private SessionFactory session_factory;
  private Session session;
  private Transaction transaction;

  public SessionHandler(final Configuration _configuration)
  {
    configuration = _configuration;
    session_factory = null;
    session = null;
    transaction = null;
  }

  public final Session setup ()
  {
    session_factory = createSessionFactory();
    session = createSession(session_factory);
    beginTransaction();

    return session;
  }

  public final void shutdown ()
  {
    rollback();
    closeSession();
    closeSessionFactory();
  }

  private SessionFactory createSessionFactory ()
  {
    configuration.addProperties(System.getProperties());

    final ServiceRegistry service_registry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties()).build();

    return configuration.buildSessionFactory(service_registry);
  }

  public final SessionFactory getSessionFactory ()
  {
    return session_factory;
  }

  public final void closeSessionFactory ()
  {
    try {
      if (session_factory != null) {
        session_factory.close();
        session_factory = null;
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public final Session createSession (final SessionFactory factory)
  {
    return factory.openSession();
  }

  public final Session getSession ()
  {
    if (session == null) {
      setup();
    }

    return session;
  }

  public final void closeSession ()
  {
    try {
      if (session != null) {
        session.close();
        session = null;
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public final Transaction beginTransaction ()
  {
    transaction = session.beginTransaction();

    return transaction;
  }

  public final void commit ()
  {
    if (transaction != null) {
      transaction.commit();
      transaction = null;
    }
  }

  public final void saveAndBegin ()
  {
    commit();
    session.clear();
    beginTransaction();
  }

  public final void rollback ()
  {
    if (transaction != null) {
      try {
        transaction.rollback();
        transaction = null;
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  public final void rollbackAndBegin ()
  {
    rollback();
    session.clear();
    beginTransaction();
  }
}
