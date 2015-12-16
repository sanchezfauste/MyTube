package myTube;

import coloredString.ColoredString;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import mobileAgent.Node;

/**
 * Represents a Content of the Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class Content implements Serializable {

    private final String key;
    private final String title;
    private final String description;
    private final Node node;
    private byte[] fileBytes;
    private String fileName;

    /**
     * Creates a new Content instance
     *
     * @param key key of the Content
     * @param title title of the Content
     * @param description description of the Content
     */
    public Content(String key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.node = null;
    }

    /**
     * Creates a new Content instance
     *
     * @param key key of the Content
     * @param title title of the Content
     * @param description description of the Content
     * @param node Node representing the server that has de content null if is
     * current server
     */
    public Content(String key, String title, String description, Node node) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.node = node;
    }

    /**
     * Get Content's key
     *
     * @return key of Content
     */
    public String getKey() {
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
     * Get Node representing the server that has de content null if is current
     * server
     *
     * @return Node representing the server that has de content
     */
    public Node getNode() {
        return node;
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

    public boolean loadContent(String file) {
        try {
            fileBytes = Files.readAllBytes(Paths.get(file));
            fileName = new File(file).getName();
            return true;
        } catch (IOException ex) {
            ColoredString.printlnError("Can't load content file: " + file);
            return false;
        }
    }

    public boolean loadContent() {
        try {
            fileName = new File("contents/" + key).listFiles()[0].getName();
            fileBytes = Files.readAllBytes(Paths.get("contents/"
                    + key + "/" + fileName));
            return true;
        } catch (IOException ex) {
            ColoredString.printlnError("Can't load content file: " + fileName);
            return false;
        }
    }

    public boolean writeContent() {
        try {
            Files.createDirectories(Paths.get("contents/" + key));
        } catch (IOException ex) {
            ColoredString.printlnError("Can't create contents folder");
            return false;
        }
        try {
            Files.write(Paths.get("contents/" + key + "/" + fileName), fileBytes);
        } catch (IOException ex) {
            ColoredString.printlnError("Can't write content file: " + fileName);
            return false;
        }
        return true;
    }

}
