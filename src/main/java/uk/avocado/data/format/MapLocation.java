package uk.avocado.data.format;

import uk.avocado.Main;
import uk.avocado.model.User;

import java.util.List;

public class MapLocation {

  private Double latitude;
  private Double longitude;
  private List<HelpArea> helpAreas;

  public MapLocation() {
  }

  public MapLocation(User user) {
    this.latitude = user.getLatitude();
    this.longitude = user.getLongitude();
    this.helpAreas = Main.databaseManager.getHelpAreasForUser(user.getUsername());
  }

  public MapLocation(double latitude, double longitude, List<HelpArea> helpAreas) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.helpAreas = helpAreas;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
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

  public List<HelpArea> getHelpAreas() {
    return helpAreas;
  }
}
