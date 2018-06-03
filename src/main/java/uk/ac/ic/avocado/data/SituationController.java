package uk.ac.ic.avocado.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Situation;

import java.util.List;

@RestController
@RequestMapping("/situation")
public class SituationController {

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Situation>> getSituations(){
      return ResponseEntity.ok(Main.databaseManager.getAllSituations());
    }
}
