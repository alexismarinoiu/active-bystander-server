package uk.avocado;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.sun.media.jfxmedia.logging.Logger;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.ClassPathResource;
import uk.avocado.database.DatabaseManager;
import uk.avocado.database.DatabaseSessionFactory;
import uk.avocado.database.FlywayIntegrator;
import uk.avocado.database.HibernateSessionFactoryAdapter;
import uk.avocado.messaging.MessagingManager;
import uk.avocado.notifications.DatabaseTokenStore;
import uk.avocado.notifications.HashMapTokenStore;
import uk.avocado.notifications.PushNotificationManager;

@SpringBootApplication
public class Main {

  public static DatabaseManager databaseManager;
  public static PushNotificationManager pushMan;
  public static MessagingManager messMan;

  public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

    final Configuration.Type environment = Configuration.getInstance().getCurrent();

    final BootstrapServiceRegistry bootstrapRegistry = new BootstrapServiceRegistryBuilder()
//        .applyIntegrator(new FlywayIntegrator())
        .build();
    final StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder(
        bootstrapRegistry).configure("hibernate.cfg.xml").build();

    final SessionFactory sessionFactory;
    try {
      sessionFactory = new MetadataSources(standardRegistry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      e.printStackTrace();
      StandardServiceRegistryBuilder.destroy(standardRegistry);
      throw new RuntimeException("Unable to create Session Factory");
    }

    final DatabaseSessionFactory dbSessionFactory = new HibernateSessionFactoryAdapter(sessionFactory);
    databaseManager = new DatabaseManager(dbSessionFactory);

    // Set up for Notification Delivery
    pushMan = new PushNotificationManager(environment, new DatabaseTokenStore(dbSessionFactory));
    messMan = new MessagingManager(databaseManager, pushMan);

    new SpringApplicationBuilder()
        .sources(Main.class)
        .properties(new HashMap<String, Object>() {{
          put("server.port", environment == Configuration.Type.PRODUCTION ? 8080 : 8081);
        }})
        .listeners((ApplicationListener<ContextClosedEvent>) applicationEvent -> {
          pushMan.shutdown();
        })
        .run(args);
  }
}
