package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "helpArea")
public class HelpArea {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "username")
  private String username;

  @Column(name = "helpAreaId")
  private int helpAreaId;
}
