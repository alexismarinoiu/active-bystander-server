package uk.avocado.data.format;

public class ProfileImage {

  private String path;
  private String sha1;

  public ProfileImage() {
  }

  public ProfileImage(String path, String sha1) {
    this.path = path;
    this.sha1 = sha1;
  }

  public String getPath() {
    return path;
  }

  public String getSha1() {
    return sha1;
  }
}
