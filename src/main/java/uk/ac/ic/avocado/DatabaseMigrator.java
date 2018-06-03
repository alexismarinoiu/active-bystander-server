package uk.ac.ic.avocado;

import org.flywaydb.core.Flyway;

public class DatabaseMigrator {
  public static void main(String[] args) {
    final Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:postgresql://db.doc.ic.ac.uk:5432/g1727128_u", "g1727128_u", "b2eWPGJUes");
    flyway.setLocations("filesystem:src/main/resources/db/migrations");
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();
  }
}
