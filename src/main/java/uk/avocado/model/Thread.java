package uk.avocado.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "thread")
public class Thread {
  @Id
  @Column(name = "threadId")
  private String threadId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  public String getThreadId() {
    return threadId;
  }

  public Status getStatus() {
    return status;
  }

}
