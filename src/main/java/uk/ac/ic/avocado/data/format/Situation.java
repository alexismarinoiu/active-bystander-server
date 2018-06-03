package uk.ac.ic.avocado.data.format;

public class Situation {

    private final String id;
    private final String html;

    public Situation(String id, String html) {
        this.id = id;
        this.html = html;
    }

    public String getId() {
        return id;
    }

    public String getHtml() {
        return html;
    }
}
