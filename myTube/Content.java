package myTube;

import java.io.Serializable;

public class Content implements Serializable {

    private final long key;
    private final String title;
    private final String description;

    public Content(long key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
    }

    public long getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TITLE: " + title + "\nDescription: " + description;
    }

}
