package uk.ac.ic.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ic.avocado.data.format.Example;

@RestController
@RequestMapping("/example")
public class ExampleController {

  @RequestMapping(method={RequestMethod.GET})
  public ResponseEntity<Example> getExample() {
    return new ResponseEntity<>(new Example(), HttpStatus.OK);
  }

}
