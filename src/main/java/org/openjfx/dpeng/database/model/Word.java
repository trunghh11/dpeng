package org.openjfx.dpeng.database.model;

public class Word {
    private String key;
    private String textDescription;
    private String htmlDescription;

    public Word() {
    }

    public Word(String key, String textDescription, String htmlDescription) {
        this.key = key;
        this.textDescription = textDescription;
        this.htmlDescription = htmlDescription;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTextDescription() {
        return this.textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getHtmlDescription() {
        return this.htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

}
