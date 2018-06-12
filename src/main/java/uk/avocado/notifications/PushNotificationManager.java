package uk.avocado.notifications;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.ApnsPushNotification;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import org.springframework.core.io.ClassPathResource;
import uk.avocado.Configuration;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PushNotificationManager {
  private ApnsClient apnsClient;
  private TokenStore tokenStore;

  public PushNotificationManager(@NotNull final Configuration.Type environment, @NotNull final TokenStore tokenStore)
          throws IOException, InvalidKeyException, NoSuchAlgorithmException {
    this.tokenStore = tokenStore;
    System.out.println("[PENDING] Configuring APNs Client");
    final File key = new ClassPathResource("aps.p8").getFile();
    apnsClient = new ApnsClientBuilder().setApnsServer(environment == Configuration.Type.PRODUCTION ? ApnsClientBuilder.PRODUCTION_APNS_HOST : ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
            .setSigningKey(ApnsSigningKey.loadFromPkcs8File(key, "QJQGH9NF7F", "2D4SS87B6W"))
            .build();
    System.out.println("[DONE] Configuring APNs Client");
  }

  public void shutdown() {
    // Allow notifications to be sent before shutting down
    try {
      apnsClient.close().await();
    } catch (InterruptedException e) {}
  }

  /**
   * Register a user for receiving notifications.
   * @param username Username of notification recipient.
   * @param token Token of user.
   */
  public void register(@NotNull final String username, @NotNull final String token) {
    tokenStore.setToken(username, token);
  }

  /**
   * Send a payload to a specific user, if they have registered with the server.
   * @param username Username of the recipient.
   * @param payload Payload to send to the user.
   * @return Whether user existed. Does not indicate status.
   */
  public boolean send(@NotNull final String username, @NotNull final String payload) {
    // Prerequisites - if not met, return false
    final String token = tokenStore.getToken(username);
    if (token == null) {
      return false;
    }

    final ApnsPushNotification notification =
            new SimpleApnsPushNotification(token, "uk.ac.imperial.Bystander", payload);
    apnsClient.sendNotification(notification);
    return true;
  }

}
