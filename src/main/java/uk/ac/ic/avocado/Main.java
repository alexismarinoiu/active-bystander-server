package uk.ac.ic.avocado;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.HashMap;

@SpringBootApplication
public class Main {
  public static void main(String[] args) throws IOException {

    final Configuration.Type environment = Configuration.getInstance().getCurrent();

    final Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:postgresql://db.doc.ic.ac.uk:5432/g1727128_u", "g1727128_u", "b2eWPGJUes");
    flyway.setLocations("filesystem:src/main/resources/db/migrations");
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();

    final SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();
    final DatabaseManager db = new DatabaseManager(sessionFactory);
    final int port = environment == Configuration.Type.PRODUCTION ? 8080 : 8081;

    new SpringApplicationBuilder()
        .sources(Main.class)
        .properties(new HashMap<String, Object>() {{
          put("server.port", port);
        }})
        .run(args);
  }
}
