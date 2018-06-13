package uk.avocado.data;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.*;
import uk.avocado.data.format.Thread;

@RestController
@RequestMapping("/thread")
public class ThreadController {

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Thread>> getAllThreads(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.databaseManager.getAllThreadsForUser(request.getUsername()));
  }

  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity<Thread> createThread(HttpServletRequest givenRequest,
      @RequestBody Location location) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    try {
      return ResponseEntity.ok(Main.messMan.createThread(request.getUsername(), location));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }

  @RequestMapping(value = "/{threadId}", method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getAllMessages(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.messMan.getAllUserMessagesForThread(request.getUsername(), threadId));
  }

  @RequestMapping(value = "/{threadId}/last-message")
  public ResponseEntity<Message> getLastMessage(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return ResponseEntity.ok(Main.messMan.getLastUserMessageForThread(request.getUsername(), threadId));
  }

  @RequestMapping(value = "/{threadId}", method = RequestMethod.PUT)
  public ResponseEntity<Message> sendMessage(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId,
      @RequestBody SentMessage message) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);

    // Sender & timestamp are ignored
    if (!Main.databaseManager.isUserThreadParticipant(request.getUsername(), threadId)) {
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity.ok(
            Main.messMan.sendMessage(request.getUsername(), message.getSeq(), message.getContent(), threadId));
  }

  @RequestMapping(value = "/{threadId}", method = RequestMethod.DELETE)
  public ResponseEntity<Thread> deleteThread(HttpServletRequest givenRequest,
                                             @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    final Thread thread = Main.databaseManager.deleteThreadWithUsername(threadId, request.getUsername());
    if (thread == null) {
      //Thread not found
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(thread);
  }

  @RequestMapping(value = "{threadId}/participant", method = RequestMethod.DELETE)
  public ResponseEntity<Participant> deleteParticipant(HttpServletRequest givenRequest,
                                                       @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    final Participant participant = Main.databaseManager
        .deleteParticipant(threadId, request.getUsername());
    if (participant == null) {
      //Participant not found
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(participant);
  }

  @RequestMapping(value = "{threadId}/message", method = RequestMethod.DELETE)
  public ResponseEntity deleteConversation(HttpServletRequest givenRequest,
                                              @PathVariable("threadId") String threadId) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    final Object object = Main.databaseManager.deleteUserFromConversation(threadId, request.getUsername());
    if (object == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(object);
  }

  @RequestMapping(value = "/{threadId}/accept", method = RequestMethod.PUT)
  public ResponseEntity<Thread> acceptThread(HttpServletRequest givenRequest,
      @PathVariable("threadId") String threadId) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    try {
      if (Main.databaseManager.isUserCreatorOfThread(username, threadId)) {
        return ResponseEntity.status(401).build();
      }
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(404).build();
    }
    return ResponseEntity.ok(Main.databaseManager.acceptThread(username, threadId));
  }
}
