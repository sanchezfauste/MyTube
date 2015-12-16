package server;

import coloredString.ColoredString;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import myTube.Content;

/**
 * Manages the DB that stores the Contents of the Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class ContentDB {

    private Connection db;

    /**
     * Creates a ContentDB and connects to dbName
     *
     * @param dbName name of sqlite database
     */
    public ContentDB(String dbName) {
        connect(dbName);
    }

    /**
     * Connects to the specified DB
     *
     * @param dbName name of sqlite database
     */
    public void connect(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            db.setAutoCommit(false);
        } catch (Exception ex) {
            ColoredString.printlnError("Can not connect to DB " + dbName);
            System.exit(1);
        }
        createContentTable();
        ColoredString.printlnSuccess("Server connected to DB " + dbName);
    }

    /**
     * Disconnects from the DB
     */
    public void disconnect() {
        try {
            db.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Can not disconnect from DB");
        }
        ColoredString.printlnSuccess("Server disconnected from DB");
    }

    private void createContentTable() {
        try {
            Statement stmt = db.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS CONTENT "
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Key TEXT UNIQUE NOT NULL, "
                    + "Title TEXT UNIQUE NOT NULL, "
                    + "Description TEXT NOT NULL)";
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Can not create table Content on DB");
            System.exit(1);
        }
    }

    /**
     * Get a Content from key or null if not exist
     *
     * @param key key of the Content
     * @return Content with specified key
     */
    public Content getContentFromKey(String key) {
        Content c = null;
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE Key = " + key + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                c = new Content(rs.getString("Key"), rs.getString("Title"),
                        rs.getString("Description"));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting content from key " + key);
        }
        return c;
    }

    /**
     * Get a Content from matching title or null if not exist
     *
     * @param title title of the Content
     * @return Content matching title
     */
    public Content getContentFromTitle(String title) {
        Content c = null;
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE Title = \""
                    + title + "\";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                c = new Content(rs.getString("Key"), rs.getString("Title"),
                        rs.getString("Description"));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting content from title "
                    + title);
        }
        return c;
    }

    /**
     * Get all Contents with a title that matches a keyword
     *
     * @param keyword keyword used to search Contents
     * @return list of matching Contents
     */
    public List<Content> getContentsFromKeyword(String keyword) {
        List<Content> contents = new ArrayList<Content>();
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE Title LIKE \'%"
                    + keyword + "%\';";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contents.add(new Content(rs.getString("Key"), rs.getString("Title"),
                        rs.getString("Description")));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting contents from title "
                    + keyword);
        }
        return contents;
    }

    /**
     * Upload a new content to the Server and returns a Content object or null
     * if the Content can't be added
     *
     * @param c the Content to add
     * @return true if the content was added correctly
     */
    public boolean addContent(Content c) {
        try {
            Statement stmt = db.createStatement();
            String query = "INSERT INTO CONTENT "
                    + "(Key, Title, Description) "
                    + "VALUES (\"" + c.getKey() + "\", \""
                    + c.getTitle() + "\", \""
                    + c.getDescription() + "\");";
            stmt.executeUpdate(query);
            stmt.close();
            db.commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}
