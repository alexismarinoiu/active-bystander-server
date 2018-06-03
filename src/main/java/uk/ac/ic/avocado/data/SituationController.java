package uk.ac.ic.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Advice;

import java.util.List;

@RestController
@RequestMapping("/situation")
public class SituationController {

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Advice>> getSituations(){
        return new ResponseEntity<List<Advice>>(Main.databaseManager.getAllSituations(), HttpStatus.OK);
    }
}
