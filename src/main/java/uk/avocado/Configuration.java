package uk.avocado;

public class Configuration {

  private static Configuration instance = null;
  private Type current;

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

  public enum Type {
    STAGING,
    PRODUCTION
  }
}
