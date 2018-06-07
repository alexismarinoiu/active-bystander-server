package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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
