package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Configuration;
import uk.avocado.Main;
import uk.avocado.data.format.HelpArea;
import uk.avocado.data.format.ProfileImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;

@RestController
@RequestMapping("/profile")
public class ProfileController {

  private static Path getStaticPath() {
    switch (Configuration.getInstance().getCurrent()) {
      case STAGING:
        return Paths.get("/var/www/html/staging/static/profile");
      case PRODUCTION:
        return Paths.get("/var/www/html/production/static/profile");
    }

    return null;
  }

  private static String validImage(final Path path) throws IOException {
    // Work out the type of file
    final ImageInputStream iis = ImageIO.createImageInputStream(
            new BufferedInputStream(new FileInputStream(path.toFile())));
    final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
    final List<String> formats = new ArrayList<>();
    while (readers.hasNext()) {
      final ImageReader reader = readers.next();
      formats.add(reader.getFormatName().toLowerCase());
    }

    // Only png and jpeg are considered valid
    if (formats.contains("png")) {
      return "png";
    }

    if (formats.contains("jpeg")) {
      return "jpeg";
    }

    return null;
  }

  @RequestMapping(value = "/helparea",method = {RequestMethod.GET})
  public ResponseEntity<List<HelpArea>> getHelpAreasForUser(HttpServletRequest givenRequest) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    return ResponseEntity.ok(Main.databaseManager.getHelpAreasForUser(username));
  }

  @RequestMapping(value = "/helparea", method = {RequestMethod.DELETE})
  public ResponseEntity<HelpArea> deleteHelpAreaForUser(HttpServletRequest givenRequest,
                                                        @RequestParam(value = "situation") String situation) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    final HelpArea helpArea = Main.databaseManager.deleteHelpAreaForUser(username, situation);
    if (helpArea == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(helpArea);
  }
  @RequestMapping(value = "/helparea", method = {RequestMethod.POST})
  public ResponseEntity<HelpArea> addHelpAreaForUser(HttpServletRequest givenRequest,
                                                     @RequestParam(value = "situation") String situation) {
    final String username = new AvocadoHttpServletRequest(givenRequest).getUsername();
    Main.databaseManager.addHelpAreaForUser(username, situation);
    return ResponseEntity.ok(new HelpArea(username, situation));
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

    final String format = validImage(path);
    if (format == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    final Path destinationPath = getStaticPath().resolve(String.format("%s.%s", sha1, format));
    Files.move(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);

    final ProfileImage profileImage = new ProfileImage(destinationPath.toString(), sha1);
    return ResponseEntity.ok(profileImage);
  }
}
