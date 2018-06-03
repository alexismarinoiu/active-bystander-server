package uk.ac.ic.avocado.data.format;

public class Advice {

    private String id;
    private String html;

    public Advice(String id, String html) {
        this.id = id;
        this.html = html;
    }

    public String getId() {
        return id;
    }

    public String getHtml() {
        return html;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
