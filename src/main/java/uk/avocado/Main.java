package uk.avocado;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.HashMap;

@SpringBootApplication
public class Main {
  public static DatabaseManager databaseManager;

  public static void main(String[] args) throws IOException {

    final Configuration.Type environment = Configuration.getInstance().getCurrent();

    final BootstrapServiceRegistry bootstrapRegistry = new BootstrapServiceRegistryBuilder()
            //.applyIntegrator(new FlywayIntegrator())
            .build();
    final StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder(bootstrapRegistry).configure("hibernate.cfg.xml").build();

    final SessionFactory sessionFactory;
    try {
      sessionFactory = new MetadataSources(standardRegistry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      e.printStackTrace();
      StandardServiceRegistryBuilder.destroy(standardRegistry);
      throw new RuntimeException("Unable to create Session Factory");
    }

    databaseManager = new DatabaseManager(sessionFactory);

    new SpringApplicationBuilder()
        .sources(Main.class)
        .properties(new HashMap<String, Object>() {{
          put("server.port", environment == Configuration.Type.PRODUCTION ? 8080 : 8081);
        }})
        .run(args);
  }
}
