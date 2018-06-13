package uk.avocado.model;

import javax.persistence.*;

@Entity
@Table(name = "situation")
public class Situation {

  @Id
  @Column(name = "id")
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
