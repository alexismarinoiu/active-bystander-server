package uk.avocado.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.Message;
import uk.avocado.data.format.Thread;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/thread")
public class ThreadController {

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Thread>> getAllThreads(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getAllThreadsForUser(request.getUsername()));
  }

  @RequestMapping(value = "/{threadId}", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getAllMessages(HttpServletRequest givenRequest,
                                                      @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getAllMessagesForThread(request.getUsername(), threadId));
  }

  @RequestMapping(value = "/{threadId}/last-message")
  public ResponseEntity<Message> getLastMessage(HttpServletRequest givenRequest,
                                               @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    Message lastMessage = Main.databaseManager.getLastMessage(request.getUsername(), threadId);
    return ResponseEntity.ok(lastMessage != null ? lastMessage : new Message());
  }
}
