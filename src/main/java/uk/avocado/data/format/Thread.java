package uk.avocado.data.format;

import uk.avocado.model.Status;

import java.util.List;

public class Thread {

  private final String threadId;
  private final Status status;

  public Thread(uk.avocado.model.Thread thread) {
    this.threadId = thread.getThreadId();
    this.status = thread.getStatus();
  }

  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

}
