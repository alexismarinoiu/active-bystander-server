package uk.avocado.data.format;

public class Location {

  private Double latitude;
  private Double longitude;

  public Location() {
  }

  public Location(double latitude, double longitude, String username) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

}
