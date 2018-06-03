package uk.ac.ic.avocado.data.format;

public class Situation {

    private String situation;
    private String html;

    public Situation(String id, String html) {
        this.situation = id;
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
