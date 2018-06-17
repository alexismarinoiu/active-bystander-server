package uk.avocado.data.format;

import uk.avocado.Main;

public class Participant {

  private String threadId;
  private String username;

  public Participant(uk.avocado.model.Participant participant) {
    this.threadId = participant.getThreadId();
    this.username = participant.getUsername();
  }

  public String getThreadId() {
    return threadId;
  }

  public String getUsername() {
    return username;
  }

  public String getDisplayName() {
    final String ldapName = Main.ldaps.getDisplayName(getUsername());
    if (ldapName == null) {
      return username;
    }

    return ldapName;
  }
}
