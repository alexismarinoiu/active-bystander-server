package uk.avocado.database;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import uk.avocado.model.User;

public class DatabaseManagerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private final DatabaseSessionFactory sessionFactory = context.mock(DatabaseSessionFactory.class);
  private final DatabaseSession session = context.mock(DatabaseSession.class);
  private final DatabaseTransaction transaction = context.mock(DatabaseTransaction.class);

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
      public void delete(Object object) {

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
            public DatabaseQuery<R> setParameter(String param, Object value) {
              return null;
            }

            @Override
            public DatabaseQuery<R> setMaxResults(int maxResults) {
              return null;
            }

            @Override
            public void executeUpdate() {

            }
          };
        }
        return null;
      }

      @Override
      public Serializable save(Object object) {
        return null;
      }

      @Override
      public DatabaseQuery createQuery(String query) {
        return null;
      }
    };
    assertEquals(session.createQuery("FROM User", User.class).list().get(0).getUsername(),
        "aim116");
  }

  @Test
  public void queryingAllLocationsOpensSessionsAndBeginsTransaction() {
    final DatabaseManager databaseManager = new DatabaseManager(sessionFactory);
    beginTransactionBlock();
    context.checking(new Expectations() {{
      oneOf(session).createQuery("FROM User", User.class);
    }});
    closeTransactionBlock();
    databaseManager.getAllLocations();
  }

  @Test
  public void creatingTransactionBlockOpensSessionAndBeginsTransaction() {
    context.checking(new Expectations() {{
      oneOf(sessionFactory).openSession();
      will(returnValue(session));
      oneOf(session).beginTransaction();
    }});
    new TransactionBlock(sessionFactory);
  }

  @Test
  public void closingTransactionBlockCommitsTransactionAndClosesSession() {
    final TransactionBlock transactionBlock;
    beginTransactionBlock();
    context.checking(new Expectations() {{
      oneOf(transaction).commit();
      oneOf(session).close();
    }});
    transactionBlock = new TransactionBlock(sessionFactory);
    transactionBlock.close();
  }

  private void beginTransactionBlock() {
    context.checking(new Expectations() {{
      ignoring(sessionFactory).openSession();
      will(returnValue(session));
      ignoring(session).beginTransaction();
      will(returnValue(transaction));
    }});
  }

  private void closeTransactionBlock() {
    context.checking(new Expectations() {{
      ignoring(transaction).commit();
      ignoring(session).close();
    }});
  }

}