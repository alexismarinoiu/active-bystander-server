package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "participant")
public class Participant {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;

  @Column(name = "threadId")
  private String threadId;

  @Column(name = "username")
  private String username;

  public String getThreadId() {
    return threadId;
  }

  public String getUsername() {
    return username;
  }
}
