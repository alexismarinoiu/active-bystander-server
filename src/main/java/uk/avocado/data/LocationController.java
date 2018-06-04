package uk.avocado.data;

import static uk.avocado.data.LocationUtils.FILTER_RADIUS;
import static uk.avocado.data.LocationUtils.calculateDistance;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.Main;
import uk.avocado.data.format.Location;

@RestController
@RequestMapping("/location")
public class LocationController {

  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<List<Location>> getLocation(
      @RequestParam(value = "username") String username,
      @RequestParam(value = "longitude") double longitude,
      @RequestParam(value = "latitude") double latitude) {
    return ResponseEntity.ok(Main.databaseManager.getAllLocations().stream()
        .filter(loc -> !loc.getUsername().equals(username))
        .filter(loc -> calculateDistance(loc.getLatitude(), loc.getLongitude(), latitude, longitude)
            < FILTER_RADIUS)
        .collect(Collectors.toList()));
  }

  @RequestMapping(method = {RequestMethod.PUT})
  public ResponseEntity<Location> addLocation(@RequestBody Location location) {
    Main.databaseManager.addOrCreateUserWithLocation(location.getUsername(), location.getLatitude(),
        location.getLongitude());
    return ResponseEntity.ok(location);
  }

}
