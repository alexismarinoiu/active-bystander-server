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

  @RequestMapping(value = "/register", method = {RequestMethod.PUT})
  public ResponseEntity<NotificationRegistration> registerDeviceForNotification(HttpServletRequest givenRequest,
                                                                                @RequestBody NotificationRegistration registration) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);

    // Convert base64 token to base16
    final String token = DatatypeConverter.printHexBinary(Base64.getDecoder().decode(registration.getToken()));
    Main.pushMan.register(request.getUsername(), token);
    System.out.println(String.format("Registering %s with token %s!", request.getUsername(), token));

    return ResponseEntity.ok(registration);
  }
}
