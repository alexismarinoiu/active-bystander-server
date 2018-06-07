package uk.avocado.database;

import org.hibernate.SessionFactory;

public class HibernateSessionFactoryAdapter implements DatabaseSessionFactory {

  private final SessionFactory sessionFactory;

  public HibernateSessionFactoryAdapter(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public DatabaseSession openSession() {
    return new HibernateSessionAdapter(sessionFactory.openSession());
  }
}
