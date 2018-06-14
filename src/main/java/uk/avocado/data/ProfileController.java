package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.Configuration;
import uk.avocado.Main;
import uk.avocado.data.format.HelpArea;
import uk.avocado.data.format.ProfileImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

  private static Path getEnvironmentPrefix() {
    switch (Configuration.getInstance().getCurrent()) {
      case STAGING:
        return Paths.get("/var/www/html/staging");
      case PRODUCTION:
        return Paths.get("/var/www/html/production");
    }

    return null;
  }

  private static Path getStaticProfilePath() {
    return getEnvironmentPrefix().resolve("static/profile");
  }

  private static String validImage(final Path path) throws IOException {
    // Work out the type of file
    final ImageInputStream iis = ImageIO.createImageInputStream(new BufferedInputStream(new FileInputStream(path.toFile())));
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

    final Path destinationPath = getStaticProfilePath().resolve(String.format("%s.%s", sha1, format));
    Files.move(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);

    final ProfileImage profileImage = new ProfileImage(getEnvironmentPrefix().relativize(destinationPath).toString(), sha1);
    return ResponseEntity.ok(profileImage);
  }
}
