package uk.avocado.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.Participant;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/thread")
public class ThreadController {

  @RequestMapping(value = "/participant", method = RequestMethod.GET)
  public ResponseEntity<List<Participant>> getAllParticipants(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getAllParticipants(request.getUsername()));
  }

  @RequestMapping(value = "/message", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getAllMessages(@RequestParam(value = "threadId") String threadId) {
    return ResponseEntity.ok(Main.databaseManager.getAllMessages(threadId));
  }
}
