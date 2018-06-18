package uk.avocado.data;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Status;
import uk.avocado.database.DatabaseManager;

@RestController
@RequestMapping("/status")
public class StatusController {

  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<Status> getExample(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    Main.databaseManager.createUserIfDoesNotExist(request.getUsername());
    return new ResponseEntity<>(new Status(true, request.getUsername()), HttpStatus.OK);
  }

}
