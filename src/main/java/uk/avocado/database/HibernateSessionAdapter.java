package uk.avocado.database;

import org.hibernate.Session;

public class HibernateSessionAdapter implements DatabaseSession {

  private final Session session;

  public HibernateSessionAdapter(Session session) {
    this.session = session;
  }

  @Override
  public void close() {
    session.close();
  }

  @Override
  public DatabaseTransaction beginTransaction() {
    return new HibernateTransactionAdapter(session.getTransaction());
  }

  @Override
  public void saveOrUpdate(Object object) {
    session.saveOrUpdate(object);
  }

  @Override
  public <R> DatabaseQuery<R> createQuery(String query, Class<R> situationClass) {
    return new HibernateQueryAdapter<>(session.createQuery(query, situationClass));
  }
}
