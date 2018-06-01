package uk.ac.ic.avocado;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DatabaseManager {

  private final SessionFactory sessionFactory;

  public DatabaseManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void addUser(String username, double latitude, double longitude) {
    final Session session = sessionFactory.openSession();
    final Transaction transaction = session.beginTransaction();
    final User user = new User();
    user.setUsername(username);
    user.setLatitude(latitude);
    user.setLongitude(longitude);
    session.save(user);
    transaction.commit();
    session.close();
  }

}
