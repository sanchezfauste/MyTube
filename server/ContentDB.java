package server;

import coloredString.ColoredString;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import myTube.Content;

public class ContentDB {

    private Connection db;

    public ContentDB(String dbName) {
        connect(dbName);
    }

    public void connect(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            db.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException ex) {
            ColoredString.printlnError("Can not connect to DB " + dbName);
            System.exit(1);
        }
        createContentTable();
        ColoredString.printlnSuccess("Server connected to DB " + dbName);
    }

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
                    + "Title TEXT UNIQUE NOT NULL, "
                    + "Description TEXT NOT NULL)";
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Can not create table Content on DB");
            System.exit(1);
        }
    }

    public Content getContentFromKey(int key) {
        Content c = null;
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE ID = " + key + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                c = new Content(rs.getInt("ID"), rs.getString("Title"),
                        rs.getString("Description"));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting content from key " + key);
        }
        return c;
    }

    public Content getContentFromTitle(String title) {
        Content c = null;
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE Title = \""
                    + title + "\";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                c = new Content(rs.getInt("ID"), rs.getString("Title"),
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

    public List<Content> getContentsFromKeyword(String keyword) {
        List<Content> contents = new ArrayList<Content>();
        try {
            Statement stmt = db.createStatement();
            String query = "SELECT * FROM CONTENT WHERE Title LIKE \'%"
                    + keyword + "%\';";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contents.add(new Content(rs.getInt("ID"), rs.getString("Title"),
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

    public Content addContent(String title, String description) {
        try {
            Statement stmt = db.createStatement();
            String query = "INSERT INTO CONTENT "
                    + "(Title, Description) "
                    + "VALUES (\"" + title + "\", \"" + description + "\");";
            stmt.executeUpdate(query);
            stmt.close();
            db.commit();
        } catch (Exception ex) {
            return null;
        }
        return getContentFromTitle(title);
    }

}
