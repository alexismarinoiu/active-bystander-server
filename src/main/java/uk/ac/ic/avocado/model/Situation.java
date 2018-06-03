package uk.ac.ic.avocado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "situation")
public class Situation {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "html")
    private String html;

    public String getId() {
        return id;
    }

    public String getHtml() {
        return html;
    }

    public void setSituation(String id) {
        this.id = id;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
