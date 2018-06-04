package uk.avocado.data.format;

public class Participant {

  private String threadid;
  private String username;

  public Participant(String threadid, String username) {
    this.threadid = threadid;
    this.username = username;
  }

  public String getThreadid() {
    return threadid;
  }

  public String getUsername() {
    return username;
  }
}
