package uk.ac.ic.avocado;

import org.hibernate.SessionFactory;
import uk.ac.ic.avocado.data.format.Location;
import uk.ac.ic.avocado.data.format.Advice;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManager {

  private final SessionFactory sessionFactory;

  public DatabaseManager(final SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void addOrCreateUserWithLocation(String username, double latitude, double longitude) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final User user = new User(username, latitude, longitude);
      tb.getSession().saveOrUpdate(user);
    }
  }

  public List<Location> getAllLocations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM User", User.class).list().stream()
              .map(user -> new Location(user.getLatitude(), user.getLongitude(), user.getUsername()))
              .collect(Collectors.toList());
    }
  }

  public List<Advice> getAllSituations() {
      try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
          return tb.getSession().createQuery("FROM Situation", Situation.class).list().stream()
                  .map(situation -> new Advice(situation.getId(), situation.getHtml()))
                  .collect(Collectors.toList());
      }
  }
}
