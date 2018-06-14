package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Configuration;
import uk.avocado.Main;
import uk.avocado.data.format.HelpArea;
import uk.avocado.data.format.ProfileImage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/profile")
public class ProfileController {

  private static Path getStaticPrefix() {
    switch (Configuration.getInstance().getCurrent()) {
      case STAGING:
        return Paths.get("/var/www/html/staging");
      case PRODUCTION:
        return Paths.get("/var/www/html/production");
    }

    return null;
  }

  @RequestMapping(value = "/helparea",method = {RequestMethod.GET})
  public ResponseEntity<List<HelpArea>> getHelpAreasForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    return ResponseEntity.ok(Main.databaseManager.getHelpAreasForUser(username));
  }

  @RequestMapping(value = "/image", method = RequestMethod.POST)
  public ResponseEntity<ProfileImage> updateUserProfileImage(HttpServletRequest givenRequest) throws IOException {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    final Map<String, String[]> parameterMap = givenRequest.getParameterMap();
    final String sha1 = Optional.ofNullable(parameterMap.get("file.sha1")).map(strings -> {
      if (strings.length == 0) {
        return null;
      }
      return strings[0];
    }).orElse(null);
    final Path path = Optional.ofNullable(parameterMap.get("file.path")).map(strings -> {
      if (strings.length == 0) {
        return null;
      }
      return Paths.get(strings[0]);
    }).orElse(null);

    if (sha1 == null || path == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    final ProfileImage profileImage = new ProfileImage(path.relativize(getStaticPrefix()).toString(), sha1);
    return ResponseEntity.ok(profileImage);
  }
}
