package uk.avocado.database;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import uk.avocado.model.User;

public class DatabaseManagerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

//  final DatabaseSessionFactory sessionFactory = context.mock(DatabaseSessionFactory.class);

  @Test
  public void callingCreateQueryQueriesDatabase() {
    final DatabaseSession session = new DatabaseSession() {
      @Override
      public void close() {
      }

      @Override
      public DatabaseTransaction beginTransaction() {
        return null;
      }

      @Override
      public void saveOrUpdate(Object object) {
      }

      @Override
      public <R> DatabaseQuery<R> createQuery(String query, Class<R> targetClass) {
        if (query.equals("FROM User")) {
          return new DatabaseQuery<R>() {
            @Override
            public List<R> list() {
              return (List<R>) Collections.singletonList(new User("aim116", 50, 0));
            }
            @Override
            public DatabaseQuery<R> setParameter(String param, String value) {
              return null;
            }
          };
        }
        return null;
      }
    };
    assertEquals(session.createQuery("FROM User", User.class).list().get(0).getUsername(), "aim116");
  }
}