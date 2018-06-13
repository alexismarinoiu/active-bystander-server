package uk.avocado.data.format;

public class Helparea {
  private String username;
  private String helpArea;

  public Helparea(String username, String helpArea) {
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
