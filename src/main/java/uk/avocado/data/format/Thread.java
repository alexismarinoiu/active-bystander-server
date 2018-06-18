package uk.avocado.data.format;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.avocado.Main;
import uk.avocado.model.Status;

public class Thread {

  private final String threadId;
  private final Status status;
  private String title;
  private final boolean creator;
  private final String threadImage;

  public Thread(uk.avocado.model.Thread thread, String username) {
    this.threadId = thread.getThreadId();
    this.status = thread.getStatus();
    this.creator = username.equals(thread.getCreator());

    if (status == Status.ACCEPTED) {
      final List<Participant> participants = Main.databaseManager
          .getParticipantsForThread(threadId);
      title = participants.stream().filter(p -> !p.getUsername().equals(username))
              .map(Participant::getDisplayName).collect(Collectors.joining(", "));
      if (title.trim().length() == 0) {
        title = "(Nobody)";
      }

      threadImage = participants.stream().filter(p -> !p.getUsername().equals(username)).findFirst()
              .map(p -> Main.databaseManager.getUser(p.getUsername()).getProfilePicture())
              .orElse(null);
    } else {
      title = "Anonymous";
      threadImage = null;
    }
  }

  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

  public String getTitle() {
    return title;
  }

  public boolean getCreator() {
    return creator;
  }

  public String getThreadImage() {
    return threadImage;
  }
}
