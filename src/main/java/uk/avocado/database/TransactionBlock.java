package uk.avocado.database;

public class TransactionBlock implements AutoCloseable {

  private final DatabaseSession session;
  private final DatabaseTransaction transaction;

  public TransactionBlock(final DatabaseSessionFactory sessionFactory) {
    session = sessionFactory.openSession();
    transaction = session.beginTransaction();
  }

  @Override
  public void close() {
    transaction.commit();
    session.close();
  }

  public DatabaseSession getSession() {
    return session;
  }
}
