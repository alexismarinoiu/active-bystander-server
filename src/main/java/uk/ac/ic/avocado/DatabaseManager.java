package uk.ac.ic.avocado;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import uk.ac.ic.avocado.data.format.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

  public List<Location> getAllLocations() {
    final Session session = sessionFactory.openSession();
    final Transaction transaction = session.beginTransaction();
    List<Location> locations = new ArrayList<>();
    List employees = session.createQuery("FROM User").list();
    for (Iterator iterator = employees.iterator(); iterator.hasNext();){
      User user = (User) iterator.next();
      locations.add(new Location(user.getLatitude(), user.getLongitude(), user.getUsername()));
    }

    transaction.commit();
    session.close();

    return locations;
  }

}
