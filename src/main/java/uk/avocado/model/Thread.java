package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "thread")
public class Thread {

  @Id
  @Column(name = "threadid")
  private String threadid;

  @Column(name = "status")
  private Status status;

  public String getThreadid() {
    return threadid;
  }

  public Status getStatus() {
    return status;
  }
}
