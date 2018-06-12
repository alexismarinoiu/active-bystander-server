package uk.avocado.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
public class Participant implements Serializable {

  @Id
  @Column(name = "threadId")
  private String threadId;

  @Id
  @Column(name = "username")
  private String username;

  public Participant() {
  }

  public Participant(String threadId, String username) {
    this.threadId = threadId;
    this.username = username;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getThreadId() {
    return threadId;
  }

  public String getUsername() {
    return username;
  }

}
