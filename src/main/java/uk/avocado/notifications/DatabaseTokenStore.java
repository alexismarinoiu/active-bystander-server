package uk.avocado.notifications;

import uk.avocado.database.DatabaseManager;
import uk.avocado.database.DatabaseSessionFactory;
import uk.avocado.database.TransactionBlock;
import uk.avocado.model.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class DatabaseTokenStore implements TokenStore {

  private final DatabaseSessionFactory sessionFactory;

  public DatabaseTokenStore(final DatabaseSessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public String getToken(@NotNull String username) {
    return getTokenForUser(username);
  }

  @Override
  public void setToken(@NotNull String username, @NotNull String token) {
    setTokenForUser(username, token);
  }

  private String getTokenForUser(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final Optional<User> optional = tb.getSession()
              .createQuery("FROM User WHERE username = :username", User.class)
              .setParameter("username", username)
              .setMaxResults(1).list().stream()
              .findFirst();

      return optional.map(User::getApsToken).orElse(null);
    }
  }

  private void setTokenForUser(String username, String token) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final Optional<User> optional = tb.getSession()
              .createQuery("FROM User WHERE username = :username", User.class)
              .setParameter("username", username)
              .setMaxResults(1).list().stream()
              .findFirst();

      optional.ifPresent(user -> {
        user.setApsToken(token);
        tb.getSession().saveOrUpdate(user);
      });
    }
  }
}
