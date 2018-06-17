package uk.avocado.data;

import static uk.avocado.data.LocationUtils.FILTER_RADIUS;
import static uk.avocado.data.LocationUtils.calculateDistance;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Location;
import uk.avocado.data.format.MapLocation;

@RestController
@RequestMapping("/location")
public class LocationController {

  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<List<MapLocation>> getLocation(HttpServletRequest givenRequest,
                                                       @RequestParam(value = "longitude") double longitude,
                                                       @RequestParam(value = "latitude") double latitude) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    return ResponseEntity.ok(Main.databaseManager.getAllMapLocations().stream()
        .filter(loc -> !loc.getUsername().equals(username))
        .filter(loc -> calculateDistance(loc.getLatitude(), loc.getLongitude(), latitude, longitude)
        < FILTER_RADIUS)
        .collect(Collectors.toList()));
  }

  @RequestMapping(method = {RequestMethod.PUT})
  public ResponseEntity<Location> addLocation(HttpServletRequest givenRequest, @RequestBody Location location) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    Main.databaseManager.addOrCreateUserWithLocation(username, location.getLatitude(), location.getLongitude());
    return ResponseEntity.ok(location);
  }

}
