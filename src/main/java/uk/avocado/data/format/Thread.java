package uk.avocado.data.format;

import uk.avocado.Main;
import uk.avocado.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Thread {

  private final String threadId;
  private final Status status;
  private String title = "Anonymous";

  public Thread(uk.avocado.model.Thread thread, String username) {
    this.threadId = thread.getThreadId();
    this.status = thread.getStatus();

    if (status == Status.ACCEPTED) {
      List<Participant> participants = Main.databaseManager.getParticipantsForThread(threadId);

      title = participants.stream().filter(p -> !p.getUsername().equals(username))
                                   .map(Participant::getUsername)
                                   .collect(Collectors.joining(","));
    }

  }

  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

}
