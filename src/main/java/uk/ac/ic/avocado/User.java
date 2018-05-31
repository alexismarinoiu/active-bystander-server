package uk.ac.ic.avocado;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @Column(name = "username")
  private String username;

  public User() {
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
