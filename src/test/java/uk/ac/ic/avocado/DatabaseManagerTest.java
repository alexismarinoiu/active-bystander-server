package uk.ac.ic.avocado;

import org.hibernate.SessionFactory;
import org.junit.Test;
import uk.ac.ic.avocado.data.format.Location;

import java.util.List;

import static org.junit.Assert.*;

public class DatabaseManagerTest {
    private final SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();;
    DatabaseManager databaseManager = new DatabaseManager(sessionFactory);

    @Test
    public void returnAllLocationsAddedWhenPostAGETRequest() {
        databaseManager.addOrCreateUserWithLocation("Alex", 200,300);
        databaseManager.addOrCreateUserWithLocation("Emma", 400, 500);

        List<Location> locations =  databaseManager.getAllLocations();
        Location entry1 = locations.get(0);
        Location entry2 = locations.get(1);

        assertEquals(entry1.getUsername(), "Alex");
        assertEquals(entry1.getLatitude(), 200, 0.001);
        assertEquals(entry1.getLongitude(), 300, 0.001);

        assertEquals(entry2.getUsername(), "Emma");
        assertEquals(entry2.getLatitude(), 400, 0.001);
        assertEquals(entry2.getLongitude(), 500, 0.001);
    }
}