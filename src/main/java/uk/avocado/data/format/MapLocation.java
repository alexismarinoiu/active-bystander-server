package uk.avocado.data.format;

import uk.avocado.Main;
import uk.avocado.model.User;

import java.util.List;

public class MapLocation {

  private Double latitude;
  private Double longitude;
  private String username;
  private List<HelpArea> helpAreas;

  public MapLocation() {
  }

  public MapLocation(User user) {
    this.latitude = user.getLatitude();
    this.longitude = user.getLongitude();
    this.username = user.getUsername();
    this.helpAreas = Main.databaseManager.getHelpAreasForUser(username);
  }

  public MapLocation(Double latitude, Double longitude, String username, List<HelpArea> helpAreas) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.username = username;
    this.helpAreas = helpAreas;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setHelpAreas(List<HelpArea> helpAreas) {
    this.helpAreas = helpAreas;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public String getUsername() {
    return username;
  }

  public List<HelpArea> getHelpAreas() {
    return helpAreas;
  }
}
