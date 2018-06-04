package uk.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.AvocadoHttpServletRequest;
import uk.avocado.data.format.Example;
import uk.avocado.data.format.Status;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/status")
public class StatusController {

  @RequestMapping(method = { RequestMethod.GET })
  public ResponseEntity<Status> getExample(HttpServletRequest givenRequest) {
    final AvocadoHttpServletRequest request = new AvocadoHttpServletRequest(givenRequest);
    return new ResponseEntity<Status>(new Status(true, request.getUsername()), HttpStatus.OK);
  }

}
