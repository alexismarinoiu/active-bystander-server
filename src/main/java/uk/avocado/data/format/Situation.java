package uk.avocado.data.format;

import java.util.List;

public class Situation {

  private String title;
  private String html;
  private List<Situation> children;

  public Situation() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public List<Situation> getChildren() {
    return children;
  }

  public void setChildren(List<Situation> children) {
    this.children = children;
  }
}
