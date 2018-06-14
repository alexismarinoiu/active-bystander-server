package uk.avocado.data.format;

public class HelpArea {
  private String username;
  private String situation;

  public HelpArea() {
  }

  public HelpArea(String username, String situation) {
    this.username = username;
    this.situation = situation;
  }

  public String getUsername() {
    return username;
  }

  public String getSituation() {
    return situation;
  }
}
