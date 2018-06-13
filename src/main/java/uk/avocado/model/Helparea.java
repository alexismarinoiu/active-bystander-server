package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "helparea")
public class Helparea {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "username")
  private String username;

  @Column(name = "helpAreaId")
  private int helpAreaId;

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public int getHelpAreaId() {
    return helpAreaId;
  }
}
