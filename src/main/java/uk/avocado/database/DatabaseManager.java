package uk.avocado.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;

import uk.avocado.data.format.*;
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
          .sorted((t1, t2) -> {
            final Timestamp ts1 = getLastMessage(username, t1.getThreadId()).getTimestamp();
            final Timestamp ts2 = getLastMessage(username, t2.getThreadId()).getTimestamp();

            if (ts1 == null && ts2 == null) {
              return 0;
            }

            if (ts1 == null) {
              return -1;
            }

            if (ts2 == null) {
              return 1;
            }

            return ts2.compareTo(ts1);
          })
          .map(t -> new Thread(t, username))
          .collect(Collectors.toList());
    }
  }

  public uk.avocado.model.Thread getThread(String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession()
          .createQuery("FROM Thread T WHERE T.threadId = :threadId", uk.avocado.model.Thread.class)
          .setParameter("threadId", threadId)
          .setMaxResults(1).list().stream()
          .findFirst().orElse(null);
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

  public List<Participant> getParticipantsForThreadExcept(String threadId, String excludeUser) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Participant P WHERE P.threadId = :threadId " +
          "AND P.username <> :excludeUser";
      return tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
          .setParameter("threadId", threadId)
          .setParameter("excludeUser", excludeUser)
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
      return !tb.getSession()
          .createQuery("FROM Participant P WHERE P.username = :username "
              + "AND P.threadId = :threadId", uk.avocado.model.Participant.class)
          .setParameter("username", username)
          .setParameter("threadId", threadId)
          .setMaxResults(1).list().isEmpty();
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

  public Thread createOrRetrieveThread(String initiatorUsername, String targetUsername)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final List<String> usernames = Arrays.asList(initiatorUsername, targetUsername);
      final String combined = usernames.stream().sorted().reduce("", String::concat);

      final MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
      msgDigest.update(combined.getBytes("UTF-8"), 0, combined.length());
      final String threadId = DatatypeConverter.printHexBinary(msgDigest.digest());

      for (final String username : usernames) {
        addParticipantWithThreadId(threadId, username);
      }

      final String query = "FROM Thread T WHERE T.threadId = :threadId";
      final List<Thread> threads = tb.getSession().createQuery(query, uk.avocado.model.Thread.class)
          .setParameter("threadId", threadId).list().stream()
          .map(t -> new Thread(t, initiatorUsername))
          .collect(Collectors.toList());
      if (!threads.isEmpty()) {
        return threads.get(0);
      }
      uk.avocado.model.Thread thread = new uk.avocado.model.Thread(threadId, Status.HOLDING,
          initiatorUsername, new Timestamp(new Date().getTime()));
      tb.getSession().saveOrUpdate(thread);
      return new Thread(thread, initiatorUsername);
    }
  }

  private void addParticipantWithThreadId(String threadId, String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().saveOrUpdate(new uk.avocado.model.Participant(threadId, username));
    }
  }

  public Thread deleteThread(String threadId, String username) {
    final uk.avocado.model.Thread thread = getThread(threadId, username);
    if (thread == null) {
      return null;
    }
    final Thread deletingThread = new Thread(thread, username);
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().delete(thread);
    }
    return deletingThread;
  }

  private uk.avocado.model.Thread getThread(String threadId, String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Thread T WHERE T.threadId = :threadId AND T.creator = :username";
      final List<uk.avocado.model.Thread> threads = tb.getSession()
          .createQuery(query, uk.avocado.model.Thread.class)
          .setParameter("threadId", threadId)
          .setParameter("username", username).list();
      if (!threads.isEmpty()) {
        return threads.get(0);
      }
      return null;
    }
  }

  public Participant deleteParticipant(String threadId, String username) {
    final uk.avocado.model.Participant participant = getParticipant(threadId, username);
    if (participant == null) {
      return null;
    }
    final Participant deletingParticipant = new Participant(participant);
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().delete(participant);
    }
    return deletingParticipant;
  }

  private uk.avocado.model.Participant getParticipant(String threadId, String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Participant P WHERE username = :username AND threadId = :threadId";
      final List<uk.avocado.model.Participant> participants =
          tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
              .setParameter("threadId", threadId)
              .setParameter("username", username).list();
      if (!participants.isEmpty()) {
        return participants.get(0);
      }
      return null;
    }
  }

  public List<HelpArea> getHelpAreasForUser(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM HelpArea WHERE username = :username";
      return tb.getSession().createQuery(query, uk.avocado.model.HelpArea.class)
          .setParameter("username", username)
          .list().stream()
          .map(this::getHelpAreaForHelpAreaId)
          .collect(Collectors.toList());
    }
  }

  private HelpArea getHelpAreaForHelpAreaId(uk.avocado.model.HelpArea h) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Situation WHERE id = :helpAreaId";
      uk.avocado.model.Situation situation = tb.getSession()
          .createQuery(query, uk.avocado.model.Situation.class)
          .setParameter("helpAreaId", h.getSituationId())
          .list().get(0);
      if (situation == null)
        return null;
      return new HelpArea(h.getUsername(), situation.getSituation());
    }
  }

  public boolean isUserCreatorOfThread(String username, String threadId)
      throws NoSuchElementException {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final uk.avocado.model.Thread thread = getThread(threadId);
      if (thread == null) {
        throw new NoSuchElementException();
      }
      return thread.getCreator().equals(username);
    }
  }

  public Thread acceptThread(String username, String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final uk.avocado.model.Thread thread = getThread(threadId);
      thread.setStatus(Status.ACCEPTED);
      tb.getSession().saveOrUpdate(thread);
      return new Thread(thread, username);
    }
  }
}


