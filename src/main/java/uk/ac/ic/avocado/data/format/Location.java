package uk.ac.ic.avocado.data.format;

public class Location {

  private final Double latitude;
  private final Double longitude;
  private final String username;

  public Location(Double latitude, Double longitude, String username) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.username = username;
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
}
