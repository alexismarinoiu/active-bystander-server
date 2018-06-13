package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "situation")
public class Situation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "situation")
  private String situation;

  @Column(name = "html")
  private String html;

  public Situation() {
  }

  public String getSituation() {
    return situation;
  }

  public String getHtml() {
    return html;
  }

  public void setSituation(String id) {
    this.situation = situation;
  }

  public void setHtml(String html) {
    this.html = html;
  }
}
