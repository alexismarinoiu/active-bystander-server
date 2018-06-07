package uk.avocado.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Location;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.SentMessage;
import uk.avocado.data.format.Thread;

@RestController
@RequestMapping("/thread")
public class ThreadController {

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Thread>> getAllThreads(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getAllThreadsForUser(request.getUsername()));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Thread> createThread(HttpServletRequest givenRequest,
      @RequestBody Location location) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.createThread(request.getUsername(), location.getUsername()));
  }

  @RequestMapping(value = "/{threadId}", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getAllMessages(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity
        .ok(Main.databaseManager.getAllMessagesForThread(request.getUsername(), threadId));
  }

  @RequestMapping(value = "/{threadId}/last-message")
  public ResponseEntity<Message> getLastMessage(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getLastMessage(request.getUsername(), threadId));
  }

  @RequestMapping(value = "/{threadId}", method = {RequestMethod.PUT})
  public ResponseEntity<Message> sendMessage(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId,
      @RequestBody SentMessage message) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);

    // Sender & timestamp are ignored
    if (!Main.databaseManager.isUserThreadParticipant(request.getUsername(), threadId)) {
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity
        .ok(Main.databaseManager.putMessage(request.getUsername(), message.getSeq(),
            message.getContent(), threadId));
  }
}
