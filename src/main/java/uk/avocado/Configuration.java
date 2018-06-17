package uk.avocado;

public class Configuration {

  private static Configuration instance = null;
  private Type current;
  private String ldapUsername;
  private String ldapPassword;

  private Configuration() {
    switch (System.getenv("BUILD_ENV")) {
      case "STAGING":
        current = Type.STAGING;
        break;
      case "PRODUCTION":
        current = Type.PRODUCTION;
        break;
      default:
        throw new RuntimeException(
            "Please set BUILD_ENV to either STAGING or PRODUCTION prior to use!");
    }

    ldapUsername = System.getenv("LDAP_USERNAME");
    ldapPassword = System.getenv("LDAP_PASSWORD");
  }

  public static Configuration getInstance() {
    if (instance == null) {
      instance = new Configuration();
    }

    return instance;
  }

  public Type getCurrent() {
    return current;
  }

  public String getLdapUsername() {
    return ldapUsername;
  }

  public String getLdapPassword() {
    return ldapPassword;
  }

  public enum Type {
    STAGING,
    PRODUCTION
  }
}
