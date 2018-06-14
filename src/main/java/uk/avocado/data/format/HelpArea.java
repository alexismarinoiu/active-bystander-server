package uk.avocado.data.format;

import uk.avocado.Main;

public class HelpArea {
  private String username;
  private String situation;
  private int situationId;

  public HelpArea() {
  }

  public HelpArea(String username, String situation, int situationId) {
    this.username = username;
    this.situation = situation;
    this.situationId = situationId;
  }

  public HelpArea(uk.avocado.model.HelpArea helpArea) {
    this.username = helpArea.getUsername();
    this.situationId = helpArea.getSituationId();

    final Situation situationData = Main.databaseManager.getSituationForSituationId(helpArea.getSituationId());
    if (situationData != null) {
      situation = situationData.getSituation();
    }
  }

  public String getUsername() {
    return username;
  }

  public String getSituation() {
    return situation;
  }

  public int getSituationId() {
    return situationId;
  }
}
