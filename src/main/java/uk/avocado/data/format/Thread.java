package uk.avocado.data.format;

import uk.avocado.model.Status;

import java.util.List;

public class Thread {

  private final String threadId;
  private final Status status;
  private final List<Participant> participants;

  public Thread(String threadId, Status status, List<Participant> participants) {
    this.threadId = threadId;
    this.status = status;
    this.participants = participants;
  }

  public String getThreadid() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

  public List<Participant> getParticipants() {
    return participants;
  }


}
