package uk.avocado.data.format;

public class Situation {

    private final String id;
    private final String html;

    public Situation(uk.avocado.model.Situation situation) {
        this.id = situation.getId();
        this.html = situation.getHtml();
    }

    public String getId() {
        return id;
    }

    public String getHtml() {
        return html;
    }
}
