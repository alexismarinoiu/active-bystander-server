package uk.avocado.data.format;

public class Situation {

  private final String situation;
  private final String html;
  private final String group;

  public Situation(uk.avocado.model.Situation situation) {
    this.situation = situation.getSituation();
    this.html = situation.getHtml();
    this.group = situation.getGroup();
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
}
