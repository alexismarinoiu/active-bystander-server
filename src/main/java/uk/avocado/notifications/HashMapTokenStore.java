package uk.avocado.notifications;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class HashMapTokenStore implements TokenStore {

  private Map<String, String> tokens = new HashMap<String, String>();

  @Override
  public String getToken(@NotNull String username) {
    return tokens.get(username);
  }

  @Override
  public void setToken(@NotNull String username, @NotNull String token) {
    tokens.put(username, token);
  }
}
