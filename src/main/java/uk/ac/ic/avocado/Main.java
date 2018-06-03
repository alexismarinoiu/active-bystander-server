package uk.ac.ic.avocado;

import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.HashMap;

@SpringBootApplication
public class Main {
  public static DatabaseManager databaseManager;

  public static void main(String[] args) throws IOException {

    final Configuration.Type environment = Configuration.getInstance().getCurrent();

    final SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();
    databaseManager = new DatabaseManager(sessionFactory);
    final int port = environment == Configuration.Type.PRODUCTION ? 8080 : 8081;

    new SpringApplicationBuilder()
        .sources(Main.class)
        .properties(new HashMap<String, Object>() {{
          put("server.port", port);
        }})
        .run(args);
  }
}
