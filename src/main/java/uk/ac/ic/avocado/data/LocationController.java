package uk.ac.ic.avocado.data;

import org.springframework.expression.ParseException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ic.avocado.data.format.Location;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/location")
public class LocationController {

  @RequestMapping(value="/", method={RequestMethod.GET})
  public ResponseEntity<Location> getLocation(@RequestParam(value="username", defaultValue="a116") String username) {
    return new ResponseEntity<Location>(new Location(120.00,
        130.00, username), HttpStatus.OK);
  }

  @RequestMapping(value="/", method={RequestMethod.POST})
  public ResponseEntity<Location> postLocation(@RequestBody Location location){
    if (location == null) {
      return new ResponseEntity<Location>(location, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<Location>(location, HttpStatus.OK);
  }

}
