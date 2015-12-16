package myTube;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public abstract class ContentManager {

	/**
	 * Get a Content from key or null if not exist
	 *
	 * @param key
	 *            key of the Content
	 * @return Content with specified key
	 */
	public static Content getContentFromKey(long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Content content;
		try {
			content = pm.getObjectById(Content.class, key);
		} catch(JDOObjectNotFoundException ex) {
			content = null;
		} finally {
			pm.close();
		}
		return content;
	}

	/**
	 * Get a Content from matching title or null if not exist
	 *
	 * @param title
	 *            title of the Content
	 * @return Content matching title
	 */
	@SuppressWarnings("unchecked")
	public static Content getContentFromTitle(String title) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Content.class);
		q.setFilter("title == \"" + title + "\"");
		List<Content> contents;
		try {
			contents = (List<Content>) q.execute();
		} finally {
			q.closeAll();
		}
		return (contents.isEmpty() ? null : contents.get(0));
	}

	/**
	 * Get all Contents with a title that matches a keyword
	 *
	 * @param keyword
	 *            keyword used to search Contents
	 * @return list of matching Contents
	 */
	@SuppressWarnings("unchecked")
	public static List<Content> getContentsFromKeyword(String keyword) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Content.class);
		//q.setFilter("title.startsWith(\"" + keyword + "\")");
		//q.setFilter("title.contains(\"" + keyword + "\")");
		List<Content> contents = new ArrayList<Content>();
		try {
			//contents = (List<Content>) q.execute();
			for (Content c : (List<Content>) q.execute()) {
				if (c.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
					contents.add(c);
				}
			}
		} finally {
			q.closeAll();
		}
		return contents;
	}

	/**
	 * Upload a new content to the Server and returns a Content object or null
	 * if the Content can't be added
	 *
	 * @param title title of the Content
     * @param description description of the Content
     * @param host host of the Content owner
     * @param port port of the Content owner
     * @param registryName registryName of the Content owner
	 */
	public static Content addContent(String title, String description, 
			String host, int port, String registryName) {
		if (getContentFromTitle(title) != null) return null;
		Content c = new Content(title, description, host, port, registryName);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(c);
		} finally {
			pm.close();
		}
		return c;
	}

}
