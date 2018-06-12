package uk.avocado.messaging;

import uk.avocado.data.format.Location;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.Thread;
import uk.avocado.database.DatabaseManager;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MessagingManager {

  private final DatabaseManager databaseManager;

  public MessagingManager(final DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public Message sendMessage(final String username, final int sequenceNumber, final String message, final String threadId) {
    return databaseManager.putMessage(username, sequenceNumber, message, threadId);
  }

  public List<Thread> getAllThreadsForUser(final String username) {
    return databaseManager.getAllThreadsForUser(username);
  }

  public Thread createThread(final String username, final Location location)
          throws UnsupportedEncodingException, NoSuchAlgorithmException {
    return databaseManager.createOrRetrieveThread(username, location.getUsername());
  }

  public List<Message> getAllUserMessagesForThread(final String username, final String threadId) {
    return databaseManager.getAllMessagesForThread(username, threadId);
  }

  public Message getLastUserMessageForThread(final String username, final String threadId) {
    return databaseManager.getLastMessage(username, threadId);
  }
}
