package uk.ac.ic.avocado;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
  @Id
  @Column(name = "username")
  private String username;

  @Column(name = "latitude")
  private double latitude;

  @Column(name = "longitude")
  private double longitude;

  User() {
  }

  User(String username, double latitude, double longitude) {
    this.username = username;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
