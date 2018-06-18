package uk.avocado.data.format;

public class HelpeeLocation {
  private Location location;
  private String situation;

  public HelpeeLocation() {

  }

  public HelpeeLocation(Location location, String situation) {
    this.location = location;
    this.situation = situation;
  }

  public Location getLocation() {
    return location;
  }

  public String getSituation() {
    return situation;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setSituation(String situation) {
    this.situation = situation;
  }
}
