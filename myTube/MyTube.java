package myTube;

import mobileAgent.MobileAgent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Functions that the Server offers to the Clients
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public interface MyTube extends Remote {

    /**
     * Get a Content from key or null if not exist
     *
     * @param key key of the Content
     * @return Content with specified key
     * @throws RemoteException if can't make the petition to the Server
     */
    public Content getContentFromKey(int key) throws RemoteException;

    /**
     * Get a Content from matching title or null if not exist
     *
     * @param title title of the Content
     * @return Content matching title
     * @throws RemoteException if can't make the petition to the Server
     */
    public Content getContentFromTitle(String title) throws RemoteException;

    /**
     * Get all Contents with a title that matches a keyword
     *
     * @param keyword keyword used to search Contents
     * @return list of matching Contents
     * @throws RemoteException if can't make the petition to the Server
     */
    public List<Content> getContentsFromKeyword(String keyword)
            throws RemoteException;

    /**
     * Upload a new content to the Server and returns a Content object or null
     * if the Content can't be added
     *
     * @param title title of the Content
     * @param description description of the Content
     * @return the added Content
     * @throws RemoteException if can't make the petition to the Server
     */
    public Content uploadContent(String title, String description)
            throws RemoteException;

    /**
     * Add a MyTubeCallback object into the callback objects lists
     *
     * @param callbackObject callbackObject to be added on callback list
     * @throws RemoteException if can't make the petition to the Server
     */
    public void addCallback(MyTubeCallback callbackObject)
            throws RemoteException;

    /**
     * Removes a MyTubeCallback object from the callback objects list
     *
     * @param callbackObject callbackObject to remove from callback list
     * @throws RemoteException if can't make the petition to the Server
     */
    public void removeCallback(MyTubeCallback callbackObject)
            throws RemoteException;

    /**
     * Send the MobileAgent
     *
     * @param agent MobileAgent to be sended
     * @throws RemoteException if can't make the petition to the Server
     */
    public void sendAgent(MobileAgent agent) throws RemoteException;

}
