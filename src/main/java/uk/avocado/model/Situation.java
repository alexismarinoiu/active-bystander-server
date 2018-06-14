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

  @Column(name = "group")
  private String group;

  public Situation() {
  }

  public int getId() {
    return id;
  }

  public String getSituation() {
    return situation;
  }

  public String getHtml() {
    return html;
  }

  public String getGroup() {
    return group;
  }

  public void setSituation(String situation) {
    this.situation = situation;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public void setGroup(String group) {
    this.group = group;
  }
}
