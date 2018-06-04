package uk.avocado.data.format;

import uk.avocado.model.Status;

public class Thread {

  private String threadid;
  private Status status;

  public Thread(String threadid, Status status) {
    this.threadid = threadid;
    this.status = status;
  }

  public String getThreadid() {
    return threadid;
  }

  public Status getStatus() {
    return status;
  }
}
