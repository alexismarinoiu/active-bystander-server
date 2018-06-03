package uk.ac.ic.avocado;

import org.flywaydb.core.Flyway;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import java.util.logging.Logger;

public class FlywayIntegrator implements Integrator {

  public static final Logger logger = Logger.getLogger("FlywayIntegrator");

  @Override
  public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    final Flyway flyway = new Flyway();
//    flyway.setBaselineOnMigrate(true);
    flyway.setDataSource("jdbc:postgresql://db.doc.ic.ac.uk:5432/g1727128_u", "g1727128_u", "b2eWPGJUes");
    flyway.setLocations("classpath:db/migration");
    flyway.setValidateOnMigrate(true);
    flyway.migrate();
  }

  @Override
  public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

  }
}
