package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
public class Participant {

  @Column(name = "threadid")
  private String threadid;

  @Column(name = "username")
  private String username;

  public String getThreadid() {
    return threadid;
  }

  public String getUsername() {
    return username;
  }
}
