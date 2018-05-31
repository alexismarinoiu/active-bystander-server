package uk.ac.ic.avocado;

import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DatabaseManager {

  private final SessionFactory sessionFactory;

  public DatabaseManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public static void not_main(String[] args) {
    final Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:postgresql://db.doc.ic.ac.uk:5432/g1727128_u", "g1727128_u", "b2eWPGJUes");
    flyway.setLocations("filesystem:src/main/resources/db/migrations");
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();

    final DatabaseManager db = new DatabaseManager(new Configuration().configure("hibernate.cfg.xml").buildSessionFactory());

    db.addUser("alexis", 149, 123);
  }

  public void addUser(String username, double latitude, double longitude) {
    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();
    User user = new User();
    user.setUsername(username);
    user.setLatitude(latitude);
    user.setLongitude(longitude);
    session.save(user);
    transaction.commit();
    session.close();
  }

}
