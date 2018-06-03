package uk.ac.ic.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Location;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

  @RequestMapping(method={RequestMethod.GET})
  public ResponseEntity<List<Location>> getLocation(@RequestParam(value="username") String username,
                                                    @RequestParam(value="longitude") double longitude,
                                                    @RequestParam(value="latitude") double latitude) {
    // TODO: Return locations in vicinity
    return ResponseEntity.ok(Main.databaseManager.getAllLocations());
  }

  @RequestMapping(method={RequestMethod.PUT})
  public ResponseEntity<Location> addLocation(@RequestBody Location location){
    Main.databaseManager.addOrCreateUserWithLocation(location.getUsername(), location.getLatitude(), location.getLongitude());
    return ResponseEntity.ok(location);
  }

}
