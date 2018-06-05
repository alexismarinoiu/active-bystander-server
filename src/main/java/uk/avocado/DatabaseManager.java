package uk.avocado;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaQuery;
import uk.avocado.data.format.*;
import uk.avocado.data.format.Thread;
import uk.avocado.model.Status;
import uk.avocado.model.User;

import java.util.ArrayList;
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

  public List<Situation> getAllSituations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM Situation", uk.avocado.model.Situation.class).list().stream()
             .map(situation -> new Situation(situation.getId(), situation.getHtml()))
             .collect(Collectors.toList());
      }
  }

  public List<Participant> getAllParticipants(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "From Participant P WHERE P.threadId IN " +
                           "(SELECT M.threadId FROM Message M WHERE sender = :username)";
      return tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
          .setParameter("username", username)
          .list().stream()
          .map(p -> new Participant(p.getThreadId(), p.getUsername()))
          .collect(Collectors.toList());
    }
  }

  public List<Message> getAllMessages(String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "From Message M where M.threadId = :threadId ORDER BY M.timestamp, M.seq";
      return tb.getSession().createQuery(query, uk.avocado.model.Message.class)
               .setParameter("threadId", threadId)
               .list().stream()
               .map(m -> new Message(m.getSender(), m.getSeq(), m.getTimestamp(), m.getContent(), m.getThreadId()))
               .collect(Collectors.toList());
    }
  }
}
