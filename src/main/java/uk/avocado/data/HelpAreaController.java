package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.HelpArea;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class HelpAreaController {
  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<List<HelpArea>> getHelpAreasForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    return ResponseEntity.ok(Main.databaseManager.getHelpAreasForUser(username));
  }

  @RequestMapping(method = {RequestMethod.DELETE})
  public ResponseEntity<HelpArea> deleteHelpAreaForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    final HelpArea helpArea =  Main.databaseManager.deleteHelpAreaForUser(username);
    if (helpArea == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(helpArea);
  }
}
