package uk.avocado.data.format;

public class Participant {

  private String threadId;
  private String username;

  public Participant(String threadId, String username) {
    this.threadId = threadId;
    this.username = username;
  }

  public String getThreadId() {
    return threadId;
  }

  public String getUsername() {
    return username;
  }
}
