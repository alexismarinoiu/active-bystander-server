package uk.avocado.data.format;

public class HelpArea {
  private String username;
  private String helpArea;

  public HelpArea(String username, String helpArea) {
    this.username = username;
    this.helpArea = helpArea;
  }

  public String getUsername() {
    return username;
  }

  public String getHelpArea() {
    return helpArea;
  }
}
