package myTube;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Represents a Content of the Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Content {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	@Persistent
    private String title;
	@Persistent
    private String description;
	@Persistent
	private String host;
	@Persistent
    private Integer port;
	@Persistent
    private String registryName;

    /**
     * Creates a new Content instance
     *
     * @param title title of the Content
     * @param description description of the Content
     * @param host host of the Content owner
     * @param port port of the Content owner
     * @param registryName registryName of the Content owner
     */
    public Content(String title, String description, String host, int port, 
    		String registryName) {
        this.title = title;
        this.description = description;
        this.host = host;
        this.port = port;
        this.registryName = registryName;
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
    
    /**
     * Return a JSONObject representing this Content
     * @return JSONObject representing this Content
     */
    public JSONObject toJson() {
    	JSONObject json = new JSONObject();
    	try {
			json.put("key", key);
			json.put("title", title);
			json.put("description", description);
			json.put("host", host);
			json.put("port", port);
			json.put("registryName", registryName);
		} catch (JSONException e) {
			System.out.println("ERROR: Can't generate JSON");
		}
    	return json;
    }

}
