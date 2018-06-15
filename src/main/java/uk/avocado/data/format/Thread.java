package uk.avocado.data.format;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.avocado.Main;
import uk.avocado.model.Status;

public class Thread {

  private final String threadId;
  private final Status status;
  private final String title;
  private final boolean isCreator;
  private final String threadImage;

  public Thread(uk.avocado.model.Thread thread, String username) {
    this.threadId = thread.getThreadId();
    this.status = thread.getStatus();
    this.isCreator = username.equals(thread.getCreator());

    if (status == Status.ACCEPTED) {
      final List<Participant> participants = Main.databaseManager
          .getParticipantsForThread(threadId);
      final Stream<Participant> filtered = participants.stream().filter(p -> !p.getUsername().equals(username));
      title = filtered.map(Participant::getUsername).collect(Collectors.joining(", "));
      threadImage = filtered.findFirst()
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

  public boolean isCreator() {
    return isCreator;
  }

  public String getThreadImage() {
    return threadImage;
  }
}
