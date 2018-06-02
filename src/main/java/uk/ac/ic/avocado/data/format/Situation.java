package uk.ac.ic.avocado.data.format;

public class Situation {

    private String situation;
    private String html;

    public Situation(String situation){
        this.situation = situation;
    }

    public Situation(String situation, String html) {
        this.situation = situation;
        this.html = html;
    }

    public String getSituation() {
        return situation;
    }

    public String getHtml() {
        return html;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
