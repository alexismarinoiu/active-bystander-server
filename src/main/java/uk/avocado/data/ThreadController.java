package uk.avocado.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.Main;
import uk.avocado.data.format.Participant;

import java.util.List;

@RestController
@RequestMapping("/thread")
public class ThreadController {

  //search by username, return all the thread
  @RequestMapping(value = "/participant", method = RequestMethod.GET)
  public ResponseEntity<List<Participant>> getAllThreads(@RequestParam(value = "username") String username) {
    return ResponseEntity.ok(Main.databaseManager.getAllParticipants(username));
  }
}
