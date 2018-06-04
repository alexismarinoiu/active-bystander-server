package uk.avocado.data.format;

public class Location {

  private Double latitude;
  private Double longitude;
  private String username;

  public Location() {
  }

  public Location(Double latitude, Double longitude, String username) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.username = username;
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
