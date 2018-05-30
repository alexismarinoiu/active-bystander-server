import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class UserManager {

  public static void main(String[] args) {

    SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    Session session1 = factory.openSession();
    Transaction transaction1 = session1.beginTransaction();

    User alexis = new User();
    alexis.setUsername("alexis");
    User nik = new User();
    nik.setUsername("nik");
    User enyi = new User();
    enyi.setUsername("enyi");

    session1.save(alexis);
    session1.save(nik);
    session1.save(enyi);
    transaction1.commit();
    session1.close();

    Session session2 = factory.openSession();
    Transaction transaction2 = session2.beginTransaction();
    String hql = "FROM User";
    Query query = session2.createQuery(hql);
    List results = query.list();
    for (Object o : results) {
      System.out.println(((User) o).getUsername());
    }
    transaction2.commit();
    session2.close();
  }

}
