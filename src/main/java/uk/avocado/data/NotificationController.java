package uk.avocado.data;

import com.turo.pushy.apns.ApnsPushNotification;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Main;
import uk.avocado.data.format.*;
import uk.avocado.data.format.Thread;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  // TEMPORARY FOR TESTING
  private static Map<String, String> tokens = new HashMap<>();

  @RequestMapping(value = "/register", method = {RequestMethod.PUT})
  public ResponseEntity<NotificationRegistration> registerDeviceForNotification(HttpServletRequest givenRequest,
                                                                                @RequestBody NotificationRegistration registration) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);

    // Convert base64 token to base16
    final String token = DatatypeConverter.printHexBinary(Base64.getDecoder().decode(registration.getToken()));
    tokens.put(request.getUsername(), token);
    System.out.println(String.format("Registering %s with token %s!", request.getUsername(), token));

    return ResponseEntity.ok(registration);
  }

  @RequestMapping(value = "/debug", method = {RequestMethod.GET})
  public ResponseEntity<String> dispatchNotification(@RequestParam String username, @RequestParam String message) {
    final String payload = new ApnsPayloadBuilder()
            .setAlertBody("DEBUG MESSAGE")
            .setAlertSubtitle(message)
            .buildWithDefaultMaximumLength();

    if (!tokens.containsKey(username)) {
      return ResponseEntity.status(404).body(String.format("Can't find %s", username));
    }

    final String token = TokenUtil.sanitizeTokenString(tokens.get(username));
    final ApnsPushNotification notification = new SimpleApnsPushNotification(token, "uk.ac.imperial.Bystander", payload);
    try {
      final PushNotificationResponse<ApnsPushNotification> response = Main.apnsClient.sendNotification(notification).get();
      if (!response.isAccepted()) {
        return ResponseEntity.status(500).body(String.format("Rejected because %s", response.getRejectionReason()));
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(e.toString());
    }

    final String out = String.format("Dispatched to %s the message %s", username, message);
    return ResponseEntity.ok(out);
  }
}
