package server;

import coloredString.ColoredString;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import mobileAgent.Node;
import myTube.Content;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Needed functions to interact with Appengine Web Service
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class MyTubeWS {

    private final Node currentServer;
    private String wsURL;

    /**
     * Creates an instance
     *
     * @param wsConfigFile config file of web service
     * @param currentServer Node representing current server
     */
    public MyTubeWS(String wsConfigFile, Node currentServer) {
        loadConfigFile(wsConfigFile);
        this.currentServer = currentServer;
    }

    private void loadConfigFile(String wsConfigFile) {
        BufferedReader f;
        try {
            f = new BufferedReader(new FileReader(wsConfigFile));
            try {
                String line;
                while ((line = f.readLine()) != null) {
                    if (line.isEmpty() || line.charAt(0) == '#') {
                        continue;
                    }
                    wsURL = line.split(" ")[0];
                }
            } catch (IOException ex) {
                ColoredString.printlnError("Can't read config file: " + wsConfigFile);
            }
            f.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Can't access config file: " + wsConfigFile);
        }
    }

    /**
     * Get a Content from key or null if not exist
     *
     * @param key key of the Content
     * @return Content with specified key
     */
    public Content getContentFromKey(String key) {
        try {
            String urlString = wsURL + "/getcontentfromkey?key=" + key;
            URL url = new URL(urlString.replace(" ", "%20"));
            return getContentFromJsonObject(getJsonObject(url));
        } catch (Exception ex) {
            ColoredString.printlnWarning("Content with key " + key
                    + " does not exist");
            return null;
        }
    }

    /**
     * Get a Content from matching title or null if not exist
     *
     * @param title title of the Content
     * @return Content matching title
     */
    public Content getContentFromTitle(String title) {
        try {
            String urlString = wsURL + "/getcontentfromtitle?title=" + title;
            URL url = new URL(urlString.replace(" ", "%20"));
            return getContentFromJsonObject(getJsonObject(url));
        } catch (Exception ex) {
            ColoredString.printlnWarning("Content with title " + title
                    + " does not exist");
            return null;
        }
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
            String urlString = wsURL + "/getcontentsfromkeyword?keyword=" + keyword;
            URL url = new URL(urlString.replace(" ", "%20"));
            for (Object result : getJsonArray(url).toArray()) {
                contents.add(getContentFromJsonObject((JSONObject) result));
            }
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting contents from keyword "
                    + keyword);
        }
        return contents;
    }

    /**
     * Upload a new content to the Server and returns a Content object or null
     * if the Content can't be added
     *
     * @param title title of the Content
     * @param description description of the Content
     * @return the added Content
     */
    public Content addContent(String title, String description) {
        try {
            URL url = new URL(wsURL + "/addcontent");
            return getContentFromJsonObject(getJsonObject(postJsonObject(url,
                    getJsonObjectFromContent(title, description))));
        } catch (Exception ex) {
            return null;
        }
    }

    private JSONObject getJsonObject(URL url) {
        try {
            return (JSONObject) new JSONParser().parse(getJsonString(url));
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting json response");
            return new JSONObject();
        }
    }

    private JSONObject getJsonObject(String json) {
        try {
            return (JSONObject) new JSONParser().parse(json);
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting json response");
            return new JSONObject();
        }
    }

    private JSONArray getJsonArray(URL url) {
        try {
            return (JSONArray) new JSONParser().parse(getJsonString(url));
        } catch (Exception ex) {
            ColoredString.printlnError("Error getting json response");
            return new JSONArray();
        }
    }

    private String getJsonString(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder jsonString = new StringBuilder();
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception ex) {
            ColoredString.printlnError("Error doing GET request");
            return "{\"error\": \"Can't get json response\"}";
        }
    }

    private String postJsonObject(URL url, JSONObject json) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.connect();
            OutputStreamWriter wt = new OutputStreamWriter(
                    connection.getOutputStream());
            wt.write(json.toString());
            wt.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder jsonString = new StringBuilder();
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception ex) {
            ColoredString.printlnError("Error doing POST request");
            return "{\"error\": \"Can't get json response\"}";
        }
    }

    private Content getContentFromJsonObject(JSONObject content) {
        return new Content(((Long) content.get("key")).toString(),
                (String) content.get("title"),
                (String) content.get("description"),
                new Node((String) content.get("host"),
                        (int) ((long) content.get("port")),
                        (String) content.get("registryName")));
    }

    private JSONObject getJsonObjectFromContent(String title, String description) {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("title", title);
        jsonContent.put("description", description);
        jsonContent.put("host", currentServer.getHost());
        jsonContent.put("port", currentServer.getPort());
        jsonContent.put("registryName", currentServer.getRegistryName());
        return jsonContent;
    }

}
