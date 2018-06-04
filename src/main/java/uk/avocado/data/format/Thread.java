package uk.avocado.data.format;

import uk.avocado.model.Status;

import java.util.List;

public class Thread {

  private final String threadid;
  private final Status status;
  private final List<Participant> participants;

  public Thread(String threadid, Status status, List<Participant> participants) {
    this.threadid = threadid;
    this.status = status;
    this.participants = participants;
  }

  public String getThreadid() {
    return threadid;
  }

  public Status getStatus() {
    return status;
  }

  public List<Participant> getParticipants() {
    return participants;
  }
}
