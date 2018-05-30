import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserManager {

  public static void main(String[] args) {

    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    Session session = factory.openSession();
    Transaction transaction = session.beginTransaction();

    User alexis = new User("alexis");
    User nik = new User("nik");
    User enyi = new User("enyi");

    session.save(alexis);
    session.save(nik);
    session.save(enyi);
    transaction.commit();
    session.close();
  }

}
