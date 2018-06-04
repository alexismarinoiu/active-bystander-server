package uk.avocado.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.Main;
import uk.avocado.data.format.Situation;

import java.util.List;

@RestController
@RequestMapping("/situation")
public class SituationController {

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Situation>> getSituations(){
      return ResponseEntity.ok(Main.databaseManager.getAllSituations());
    }
}
