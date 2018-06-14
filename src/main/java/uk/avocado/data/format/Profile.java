package uk.avocado.data.format;

import java.util.List;

public class Profile {

  public static class Builder {
    private final Profile profile = new Profile();

    public Builder setUsername(String username) {
      profile.setUsername(username);
      return this;
    }

    public Builder setDisplayName(String displayName) {
      profile.setDisplayName(displayName);
      return this;
    }

    public Builder setProfileImage(String profileImage) {
      profile.setProfileImage(profileImage);
      return this;
    }

    public Builder setHelpAreas(List<HelpArea> helpAreas) {
      profile.setHelpAreas(helpAreas);
      return this;
    }

    public Profile build() {
      return profile;
    }
  }

  private String username;
  private String displayName;
  private String profileImage;
  private List<HelpArea> helpAreas;

  public Profile() {
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setHelpAreas(List<HelpArea> helpAreas) {
    this.helpAreas = helpAreas;
  }

  public List<HelpArea> getHelpAreas() {
    return helpAreas;
  }
}
