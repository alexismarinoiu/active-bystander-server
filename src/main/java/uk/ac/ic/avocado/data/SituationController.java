package uk.ac.ic.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Location;
import uk.ac.ic.avocado.data.format.Situation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/situation")
public class SituationController {

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Situation>> getSituations(){
        return new ResponseEntity<List<Situation>>(Main.dm.getAllSituations(), HttpStatus.OK);
    }
}
