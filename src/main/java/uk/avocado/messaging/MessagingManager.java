package uk.avocado.messaging;

import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import uk.avocado.data.format.Location;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.Participant;
import uk.avocado.data.format.Thread;
import uk.avocado.database.DatabaseManager;
import uk.avocado.notifications.PushNotificationManager;

public class MessagingManager {

  private final PushNotificationManager pushMan;
  private final DatabaseManager databaseManager;

  public MessagingManager(final DatabaseManager databaseManager,
      final PushNotificationManager pushMan) {
    this.databaseManager = databaseManager;
    this.pushMan = pushMan;
  }

  public Message sendMessage(final String username, final int sequenceNumber, final String message,
      final String threadId) {
    final List<Participant> otherParticipants = databaseManager
        .getParticipantsForThreadExcept(threadId, username);

    // Send notifications to users, ignore if they don't have their device registered
    for (final Participant participant : otherParticipants) {
      final uk.avocado.model.Thread databaseThread = databaseManager.getThread(threadId);
      if (databaseThread == null) {
        continue;
      }

      final Thread thread = new Thread(databaseThread, participant.getUsername());
      final String payload = new ApnsPayloadBuilder()
              .setAlertTitle(String.format("Message from %.20s", thread.getTitle()))
              .setAlertBody(String.format("%.500s", message))
              .setSoundFileName("default")
              .addCustomProperty("threadId", thread.getThreadId())
              .addCustomProperty("title", thread.getTitle())
              .addCustomProperty("status", thread.getStatus().toString())
              .buildWithDefaultMaximumLength();
      pushMan.send(participant.getUsername(), payload);
    }

    return databaseManager.putMessage(username, sequenceNumber, message, threadId);
  }

  public List<Thread> getAllThreadsForUser(final String username) {
    return databaseManager.getAllThreadsForUser(username);
  }

  public Thread createThread(final String username, final Location location)
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    final Thread thread = databaseManager.createOrRetrieveThread(username, location.getUsername());
    final List<Participant> otherParticipants = databaseManager
        .getParticipantsForThreadExcept(thread.getThreadId(), username);
    // This is kinda terrible
    for (final Participant participant : otherParticipants) {
      final uk.avocado.model.Thread databaseThread = databaseManager
          .getThread(thread.getThreadId());
      if (databaseThread == null) {
        continue;
      }

      final Thread userThread = new Thread(databaseThread, participant.getUsername());
      final String payload = new ApnsPayloadBuilder()
              .setAlertTitle(String.format("%.20s needs your help!", userThread.getTitle()))
              .setSoundFileName("default")
              .addCustomProperty("threadId", userThread.getThreadId())
              .addCustomProperty("title", userThread.getTitle())
              .addCustomProperty("status", userThread.getStatus().toString())
              .buildWithDefaultMaximumLength();
      pushMan.send(participant.getUsername(), payload);
    }

    return thread;
  }

  public List<Message> getAllUserMessagesForThread(final String username, final String threadId) {
    return databaseManager.getAllMessagesForThread(username, threadId);
  }

  public Message getLastUserMessageForThread(final String username, final String threadId) {
    return databaseManager.getLastMessage(username, threadId);
  }
}
