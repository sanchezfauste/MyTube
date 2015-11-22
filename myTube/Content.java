package myTube;

import java.io.Serializable;

/**
 * Represents a Content of the Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class Content implements Serializable {

    private final long key;
    private final String title;
    private final String description;

    /**
     * Creates a new Content instance
     *
     * @param key key of the Content
     * @param title title of the Content
     * @param description description of the Content
     */
    public Content(long key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
    }

    /**
     * Get Content's key
     *
     * @return key of Content
     */
    public long getKey() {
        return key;
    }

    /**
     * Get Content's title
     *
     * @return title of Content
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Content's description
     *
     * @return description of Content
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return a String representing a Content
     *
     * @return representation of a Content
     */
    @Override
    public String toString() {
        return "TITLE: " + title + "\nDescription: " + description;
    }

}
