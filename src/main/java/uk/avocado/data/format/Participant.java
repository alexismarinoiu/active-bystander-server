package uk.avocado.data.format;

public class Participant {

  private String threadId;
  private String username;

  public Participant(uk.avocado.model.Participant participant) {
    this.threadId = participant.getThreadId();
    this.username = participant.getUsername();
  }

  public String getThreadId() {
    return threadId;
  }

  public String getUsername() {
    return username;
  }
}
