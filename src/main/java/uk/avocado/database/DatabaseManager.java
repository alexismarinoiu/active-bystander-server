package uk.avocado.database;

import java.sql.Timestamp;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import uk.avocado.data.format.Location;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.Participant;
import uk.avocado.data.format.Situation;
import uk.avocado.data.format.Thread;
import uk.avocado.model.Status;
import uk.avocado.model.User;

public class DatabaseManager {

  private final DatabaseSessionFactory sessionFactory;

  public DatabaseManager(final DatabaseSessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void addOrCreateUserWithLocation(String username, double latitude, double longitude) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().saveOrUpdate(new User(username, latitude, longitude));
    }
  }

  public List<Location> getAllLocations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM User", User.class).list()
          .stream()
          .map(user -> new Location(user.getLatitude(), user.getLongitude(), user.getUsername()))
          .collect(Collectors.toList());
    }
  }

  public List<Situation> getAllSituations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM Situation", uk.avocado.model.Situation.class)
          .list()
          .stream()
          .map(Situation::new)
          .collect(Collectors.toList());
    }
  }

  public List<Thread> getAllThreadsForUser(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Thread T WHERE T.threadId IN " +
          "(SELECT P.threadId FROM Participant P WHERE username = :username)";
      return tb.getSession().createQuery(query, uk.avocado.model.Thread.class)
          .setParameter("username", username)
          .list().stream()
          .map(t -> new Thread(t, username))
          .collect(Collectors.toList());
    }
  }

  public List<Message> getAllMessagesForThread(String username, String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Message M WHERE M.threadId = :threadId AND EXISTS " +
          "(SELECT P FROM Participant P WHERE P.username = :username AND P.threadId = :threadId) " +
          "ORDER BY M.timestamp, M.seq";
      return tb.getSession().createQuery(query, uk.avocado.model.Message.class)
          .setParameter("threadId", threadId)
          .setParameter("username", username)
          .list().stream()
          .map(Message::new)
          .collect(Collectors.toList());
    }
  }

  public List<Participant> getParticipantsForThread(String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Participant P WHERE P.threadId = :threadId";
      return tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
          .setParameter("threadId", threadId)
          .list().stream().map(Participant::new)
          .collect(Collectors.toList());
    }
  }

  public Message getLastMessage(String username, String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Message M WHERE M.threadId = :threadId AND EXISTS " +
          "(SELECT P FROM Participant P WHERE P.username = :username AND P.threadId = :threadId) " +
          "ORDER BY M.timestamp DESC, M.seq";
      final List<Message> messages = tb.getSession()
          .createQuery(query, uk.avocado.model.Message.class)
          .setParameter("threadId", threadId)
          .setParameter("username", username)
          .setMaxResults(1).list().stream()
          .map(Message::new)
          .collect(Collectors.toList());
      return messages.isEmpty() ? new Message() : messages.get(0);
    }
  }

  public boolean isUserThreadParticipant(String username, String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession()
          .createQuery("FROM Participant P WHERE P.username = :username "
              + "AND P.threadId = :threadId", uk.avocado.model.Participant.class)
          .setParameter("username", username)
          .setParameter("threadId", threadId)
          .setMaxResults(1).list().stream()
          .map(p -> 1)
          .reduce((a, b) -> a + b)
          .orElse(0) == 1;
    }
  }

  public Message putMessage(String sender, int sequence, String content, String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final Timestamp timestamp = new Timestamp(new Date().getTime());
      final uk.avocado.model.Message message =
          new uk.avocado.model.Message(sender, sequence, timestamp, content, threadId);
      tb.getSession().save(message);
      return new Message(message);
    }
  }

  public Thread createThread(String initiatorUsername, String targetUsername) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      List<String> listUsernames = new ArrayList<>();
      listUsernames.add(initiatorUsername);
      listUsernames.add(targetUsername);
      String combined = listUsernames.stream().sorted().reduce("", String::concat);
      MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
      msgDigest.update(combined.getBytes("UTF-8"), 0, combined.length());
      uk.avocado.model.Thread thread = new uk.avocado.model.Thread(
          DatatypeConverter.printHexBinary(msgDigest.digest()), Status.HOLDING);
      tb.getSession().saveOrUpdate(thread);
      return new Thread(thread, initiatorUsername);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
