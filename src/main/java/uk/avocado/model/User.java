package uk.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @Column(name = "username")
  private String username;

  @Column(name = "latitude")
  private double latitude;

  @Column(name = "longitude")
  private double longitude;

  @Column(name = "apsToken")
  private String apsToken;

  @Column(name = "profilePicture")
  private String profilePicture;

  @Column(name = "iskeyhelper")
  private boolean isKeyHelper;

  public User() {
  }

  public User(String username, double latitude, double longitude) {
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

  public String getApsToken() {
    return apsToken;
  }

  public void setApsToken(String apsToken) {
    this.apsToken = apsToken;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public boolean isKeyHelper() {
    return isKeyHelper;
  }

  public void setKeyHelper(boolean keyHelper) {
    isKeyHelper = keyHelper;
  }
}
