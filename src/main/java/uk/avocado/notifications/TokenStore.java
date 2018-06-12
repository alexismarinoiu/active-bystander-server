package uk.avocado.notifications;

import javax.validation.constraints.NotNull;

public interface TokenStore {
  String getToken(@NotNull final String username);
  void setToken(@NotNull final String username, @NotNull final String token);
  boolean hasToken(@NotNull final String username);
}
