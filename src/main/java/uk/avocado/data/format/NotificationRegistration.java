package uk.avocado.data.format;

import uk.avocado.Main;
import uk.avocado.model.Status;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationRegistration {

  private String token;

  NotificationRegistration() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }
}
