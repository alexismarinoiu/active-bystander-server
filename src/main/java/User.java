import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @Column(name = "username")
  private final String username;

  public User(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
