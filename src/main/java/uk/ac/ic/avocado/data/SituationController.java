package uk.ac.ic.avocado.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ic.avocado.Main;
import uk.ac.ic.avocado.data.format.Location;
import uk.ac.ic.avocado.data.format.Situation;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/situation")
public class SituationController {

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Situation>> getSituations() {
        List<Situation> situations = new ArrayList<>();
        situations.add(new Situation("Sexual Harassment"));
        situations.add(new Situation("Physical Assault"));
        situations.add(new Situation("Verbal Assault"));
        situations.add(new Situation("Cyberbullying"));
        return new ResponseEntity<List<Situation>>(situations, HttpStatus.OK);
    }

}
