package uk.avocado.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
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
      User user = getUser(username);
      if (user == null) {
        user = new User(username, latitude, longitude);
      } else {
        user.setLatitude(latitude);
        user.setLongitude(longitude);
      }

      tb.getSession().saveOrUpdate(user);
    }
  }

  public List<MapLocation> getAllMapLocations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM User", User.class).list()
          .stream()
          .map(MapLocation::new)
          .collect(Collectors.toList());
    }
  }

  public List<uk.avocado.model.Situation> getAllSituations() {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM Situation", uk.avocado.model.Situation.class).list();
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
              return t2.getTimestamp().compareTo(t1.getTimestamp());
            }

            if (ts1 == null) {
              return ts2.compareTo(t1.getTimestamp());
            }

            if (ts2 == null) {
              return t2.getTimestamp().compareTo(ts1);
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

  public Thread deleteThreadWithUsername(String threadId, String username) {
    final uk.avocado.model.Thread thread = getThreadWithUsername(threadId, username);
    if (thread == null) {
      return null;
    }
    final Thread deletingThread = new Thread(thread, username);
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().delete(thread);
    }
    return deletingThread;
  }

  private uk.avocado.model.Thread getThreadWithUsername(String threadId, String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return getThreadWithUsername(tb, threadId, username);
    }
  }

  private uk.avocado.model.Thread getThreadWithUsername(TransactionBlock tb, String threadId,
      String username) {
    final String query = "FROM Thread T WHERE T.threadId = :threadId AND T.creator = :username";
    return tb.getSession().createQuery(query, uk.avocado.model.Thread.class)
        .setParameter("threadId", threadId)
        .setParameter("username", username)
        .setMaxResults(1).list().stream()
        .findFirst().orElse(null);
  }

  public Thread deleteThread(String threadId) {
    final uk.avocado.model.Thread thread = getThread(threadId);
    if (thread == null) {
      return null;
    }
    final Thread deletingThread = new Thread(thread, "");
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().delete(thread);
    }
    return deletingThread;
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
      return tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
          .setParameter("threadId", threadId)
          .setParameter("username", username)
          .setMaxResults(1).list().stream()
          .findFirst().orElse(null);
    }
  }

  private List<uk.avocado.model.Participant> getAllParticipantsForAThread(String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Participant P WHERE threadId = :threadId";
      return tb.getSession().createQuery(query, uk.avocado.model.Participant.class)
          .setParameter("threadId", threadId).list();
    }
  }

  public Thread removeUserFromThread(String threadId, String username) {
    try (TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      deleteParticipant(threadId, username);
      final uk.avocado.model.Thread thread = getThread(threadId);
      // Check if no other participants are left for the thread
      final List<uk.avocado.model.Participant> participants = getAllParticipantsForAThread(threadId);
      if (participants.isEmpty()) {
        final List<uk.avocado.model.Message> messages = getAllMessagesForTheThread(threadId);
        // Delete all messages, participant and the thread
        for (uk.avocado.model.Message message : messages) {
          tb.getSession().delete(message);
        }
        tb.getSession().delete(thread);
      }
      return new Thread(thread, username);
    }
  }

  private List<uk.avocado.model.Message> getAllMessagesForTheThread(String threadId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Message M WHERE M.threadId = :threadId";
      return tb.getSession().createQuery(query, uk.avocado.model.Message.class)
          .setParameter("threadId", threadId).list();
    }
  }

  public List<HelpArea> getHelpAreasForUser(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM HelpArea WHERE username = :username";
      return tb.getSession().createQuery(query, uk.avocado.model.HelpArea.class)
          .setParameter("username", username)
          .list().stream()
          .map(HelpArea::new)
          .collect(Collectors.toList());
    }
  }

  public HelpArea deleteHelpAreaForUser(String username, int situationId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM HelpArea WHERE username = :username AND situationId = :situationId";
      final List<uk.avocado.model.HelpArea> helpAreas = tb.getSession().createQuery(query, uk.avocado.model.HelpArea.class)
          .setParameter("username", username)
          .setParameter("situationId", situationId)
          .list();

      if (helpAreas.isEmpty()) {
        return null;
      }

      final uk.avocado.model.HelpArea helpArea = helpAreas.get(0);
      final HelpArea helpAreaData = new HelpArea(helpArea);
      tb.getSession().delete(helpArea);
      return helpAreaData;
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

  public void addHelpAreaForUser(String username, int situationId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Situation WHERE id = :situationId";
      final List<uk.avocado.model.Situation> situations = tb.getSession()
          .createQuery(query, uk.avocado.model.Situation.class)
          .setParameter("situationId", situationId).list();

      if (situations.isEmpty()) {
        return;
      }

      tb.getSession().save(new uk.avocado.model.HelpArea(username, situations.get(0).getId()));
    }
  }

  public uk.avocado.model.Situation getSituationForSituationId(int situationId) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final String query = "FROM Situation WHERE id = :situationId";
      final List<uk.avocado.model.Situation> situations = tb.getSession()
          .createQuery(query, uk.avocado.model.Situation.class)
          .setParameter("situationId", situationId).list();

      if (situations.isEmpty()) {
        return null;
      }
      return situations.get(0);

    }
  }

  public void setUserProfilePicture(String username, String profilePicture) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      tb.getSession().createQuery("FROM User WHERE username = :username", User.class)
              .setParameter("username", username).list().stream().forEach(u -> {
        u.setProfilePicture(profilePicture);
        tb.getSession().saveOrUpdate(u);
      });
    }
  }

  public User getUser(String username) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      return tb.getSession().createQuery("FROM User WHERE username = :username", User.class)
          .setParameter("username", username)
          .setMaxResults(1).list().stream()
          .findFirst()
          .orElse(null);
    }
  }

  public User getUserAroundRegion(double latitude, double longitude) {
    try (final TransactionBlock tb = new TransactionBlock(sessionFactory)) {
      final double delta = .000001;
      List<User> usr = tb.getSession().createQuery("FROM User WHERE (latitude BETWEEN :latlo AND :lathi) AND (longitude BETWEEN :lonlo AND :lonhi)", User.class)
              .setParameter("latlo", latitude - delta)
              .setParameter("lathi", latitude + delta)
              .setParameter("lonlo", longitude - delta)
              .setParameter("lonhi", longitude + delta)
              .list();
      return usr.stream().findFirst().orElse(null);
    }
  }
}


