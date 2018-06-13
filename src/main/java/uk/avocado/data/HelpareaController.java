package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Helparea;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/helparea")
public class HelpareaController {
  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<List<Helparea>> getHelpAreasForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    return ResponseEntity.ok(Main.databaseManager.getHelpAreasForUser(username));
  }

  @RequestMapping(method = {RequestMethod.DELETE})
  public ResponseEntity<Helparea> deleteHelpAreaForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    final Helparea helparea =  Main.databaseManager.deleteHelpAreaForUser(username);
    if (helparea == null) {
      //Participant not found
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(helparea);
  }
}
