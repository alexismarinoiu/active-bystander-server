package uk.avocado.data.format;

public class Situation {

  private final String id;
  private final String html;
  private final String group;

  public Situation(uk.avocado.model.Situation situation) {
    this.id = situation.getSituation();
    this.html = situation.getHtml();
    this.group = situation.getGroup();
  }

  public String getId() {
    return id;
  }

  public String getHtml() {
    return html;
  }

  public String getGroup() {
    return group;
  }
}
