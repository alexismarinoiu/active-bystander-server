package uk.avocado.database;

import java.io.Serializable;
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
    return new HibernateTransactionAdapter(session.beginTransaction());
  }

  @Override
  public void saveOrUpdate(Object object) {
    session.saveOrUpdate(object);
  }

  @Override
  public void delete(Object object) {
    session.delete(object);
  }

  @Override
  public <R> DatabaseQuery<R> createQuery(String query, Class<R> targetClass) {
    return new HibernateQueryAdapter<>(session.createQuery(query, targetClass));
  }

  @Override
  public Serializable save(Object object) {
    return session.save(object);
  }
}
