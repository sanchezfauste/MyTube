package server;

import coloredString.ColoredString;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import myTube.Content;
import mobileAgent.MobileAgent;
import mobileAgent.Node;
import myTube.MyTube;
import myTube.MyTubeCallback;

/**
 * Functions that the Server offers to the Clients
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class MyTubeImpl extends UnicastRemoteObject implements MyTube {

    private final Set<MyTubeCallback> callbackObjects;
    private final ContentDB contents;
    private final MyTubeWS ws;
    private final Node currentServer;

    /**
     * Creates a MyTubeImpl instance
     *
     * @param dbName name of sqlite database file
     * @param currentServer Node representing current server
     * @param wsConfigFile config file of web service
     * @throws RemoteException
     */
    public MyTubeImpl(String dbName, Node currentServer, String wsConfigFile)
            throws RemoteException {
        super();
        callbackObjects = new HashSet<MyTubeCallback>();
        contents = new ContentDB(dbName);
        ws = new MyTubeWS(wsConfigFile, currentServer);
        this.currentServer = currentServer;
    }

    @Override
    public Content getContentFromKey(String key) throws RemoteException {
        ColoredString.printlnInfo("A client make a request for key: " + key);
        Content c = contents.getContentFromKey(key);
        if (c != null) {
            ColoredString.printlnSuccess("Content found on this server!");
            c.loadContent();
            return c;
        }
        c = ws.getContentFromKey(key);
        if (c == null) {
            ColoredString.printlnError("The content was not found on Web Service");
            return null;
        }
        return getRemoteContent(c);
    }

    @Override
    public Content getContentFromTitle(String title) throws RemoteException {
        ColoredString.printlnInfo("A client make a request for title: " + title);
        Content c = contents.getContentFromTitle(title);
        if (c != null) {
            ColoredString.printlnSuccess("Content found on this server!");
            c.loadContent();
            return c;
        }
        c = ws.getContentFromTitle(title);
        if (c == null) {
            return null;
        }
        return getRemoteContent(c);
    }

    @Override
    public List<Content> getContentsFromKeyword(String keyword)
            throws RemoteException {
        ColoredString.printlnInfo("A client make a request for keyword: "
                + keyword + ". Searching contents on Web Service.");
        return ws.getContentsFromKeyword(keyword);
    }

    @Override
    public Content uploadContent(String title, String description)
            throws RemoteException {
        Content c = ws.addContent(title, description);
        if (c == null || !contents.addContent(c)) {
            ColoredString.printlnWarning("Client tried to upload an existing "
                    + "or incorrect content");
            return null;
        }
        ColoredString.printlnCyan("-- INFO: New content has been added --");
        ColoredString.printlnCyan("KEY: " + c.getKey());
        ColoredString.printlnCyan(c.toString());
        ColoredString.printlnCyan("--------------------------------------");
        notifyAllNewContent(c);
        return c;
    }

    @Override
    public void addCallback(MyTubeCallback callbackObject)
            throws RemoteException {
        callbackObjects.add(callbackObject);
        ColoredString.printlnSuccess("New client registered on callback list. ("
                + callbackObjects.size() + " clients)");
    }

    @Override
    public void removeCallback(MyTubeCallback callbackObject)
            throws RemoteException {
        callbackObjects.remove(callbackObject);
        ColoredString.printlnSuccess("A client have been unregistered "
                + "from callback list. (" + callbackObjects.size() + " clients)");
    }

    private void notifyAllNewContent(Content c) {
        for (MyTubeCallback callback : callbackObjects) {
            try {
                callback.notifyNewContent(c);
            } catch (RemoteException ex) {
                ColoredString.printlnWarning("Can not notify new content "
                        + "to a client");
            }
        }
    }

    private void notifyAllServerStopped() {
        for (MyTubeCallback callback : callbackObjects) {
            try {
                callback.notifyServerStopped();
            } catch (RemoteException ex) {

            }
        }
    }

    /**
     * Notifies the registered Clients that the Server stops
     */
    public void exit() {
        notifyAllServerStopped();
        contents.disconnect();
    }

    @Override
    public void sendAgent(MobileAgent agent) throws RemoteException {
        ColoredString.printlnPurple("[SERVER]: Welcome agent " + agent.getName());
        agent.printVisitedHosts();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ColoredString.printlnError("Thread can't sleep");
        }
        agent.execute();
    }

    private Content getRemoteContent(Content c) {
        try {
            ColoredString.printlnSuccess("Content " + c.getKey()
                    + " found on server rmi://" + c.getNode().getHost()
                    + ":" + c.getNode().getPort()
                    + ":" + c.getNode().getRegistryName());
            Registry registry = LocateRegistry.getRegistry(c.getNode().getHost(),
                    c.getNode().getPort());
            MyTube stub = (MyTube) registry.lookup(c.getNode().getRegistryName());
            ColoredString.printlnInfo("Taking the content from remote server");
            return stub.getContentFromKey(c.getKey());
        } catch (Exception ex) {
            ColoredString.printlnError("Can't get the content " + c.getKey()
                    + " from server rmi://" + c.getNode().getHost()
                    + ":" + c.getNode().getPort()
                    + ":" + c.getNode().getRegistryName());
            return null;
        }
    }

    @Override
    public boolean uploadContentFile(Content c) throws RemoteException {
        return c.writeContent();
    }

}
