package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "threadId")
  private String threadId;

  @Column(name = "username")
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
