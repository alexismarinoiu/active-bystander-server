package uk.avocado.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.avocado.Main;
import uk.avocado.data.format.Situation;

@RestController
@RequestMapping("/situation")
public class SituationController {

  @RequestMapping(method = {RequestMethod.GET})
  public ResponseEntity<List<Situation>> getSituations() {
    final List<uk.avocado.model.Situation> situations = Main.databaseManager.getAllSituations();

    final Set<String> categoryStrings = new HashSet<>();
    for (uk.avocado.model.Situation situation : situations) {
      categoryStrings.add(situation.getGroup());
    }

    final List<Situation> categories = new ArrayList<>();
    for (final String category : categoryStrings) {
      categories.add(new Situation() {{
        setTitle(category);
        final List<Situation> children = new ArrayList<>();
        for (final uk.avocado.model.Situation situation : situations) {
          if (situation.getGroup().equals(category)) {
            children.add(new Situation() {{
              setTitle(situation.getSituation());
              setHtml(situation.getHtml());
              setChildren(new ArrayList<>());
            }});
          }
        }
        setChildren(children);
      }});
    }

    return ResponseEntity.ok(categories);
  }
}
