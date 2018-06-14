package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "helparea")
public class HelpArea {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "username")
  private String username;

  @Column(name = "situationId")
  private int situationId;

  public HelpArea() {
  }

  public HelpArea(String username, int situationId) {
    this.username = username;
    this.situationId = situationId;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public int getSituationId() {
    return situationId;
  }
}
