package uk.avocado;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionBlock implements AutoCloseable {

  private final Session session;
  private final Transaction transaction;

  public TransactionBlock(final SessionFactory sessionFactory) {
    session = sessionFactory.openSession();
    transaction = session.beginTransaction();
  }

  @Override
  public void close() {
    transaction.commit();
    session.close();
  }

  public Session getSession() {
    return session;
  }
}
