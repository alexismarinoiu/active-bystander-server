package uk.avocado.data.format;

public class Status {

  public final boolean status;
  public final String username;

  public Status(boolean status, String username) {
    this.status = status;
    this.username = username;
  }

  public boolean isStatus() {
    return status;
  }

  public String getUsername() {
    return username;
  }

}
