package uk.ac.ic.avocado.data;

import org.springframework.expression.ParseException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Location;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

  @RequestMapping(method={RequestMethod.GET})
  public ResponseEntity<List<Location>> getLocation(@RequestParam(value="username", defaultValue="a116") String username) {

    return new ResponseEntity<List<Location>>(Main.dm.getAllLocations(), HttpStatus.OK);
  }

  @RequestMapping(method={RequestMethod.POST})
  public ResponseEntity<Location> postLocation(@RequestBody Location location){
    if (location == null) {
      return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
    }

    /* add user to the database*/
    Main.dm.addUser(location.getUsername(), location.getLatitude(), location.getLongitude());

    return new ResponseEntity<Location>(location, HttpStatus.OK);
  }

}
