package server;

import coloredString.ColoredString;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import myTube.Content;
import myTube.MyTube;
import myTube.MyTubeCallback;

public class MyTubeImpl extends UnicastRemoteObject implements MyTube {

    private static Set<MyTubeCallback> callbackObjects;
    private ContentDB contents;

    public MyTubeImpl() throws RemoteException {
        this("contents.sqlite");
    }

    public MyTubeImpl(String dbName) throws RemoteException {
        super();
        callbackObjects = new HashSet<MyTubeCallback>();
        contents = new ContentDB(dbName);
    }

    @Override
    public Content getContentFromKey(int key) throws RemoteException {
        ColoredString.printlnInfo("A client make a request for key: " + key);
        return contents.getContentFromKey(key);
    }

    @Override
    public Content getContentFromTitle(String title) throws RemoteException {
        ColoredString.printlnInfo("A client make a request for title: " + title);
        return contents.getContentFromTitle(title);
    }

    @Override
    public List<Content> getContentsFromKeyword(String keyword)
            throws RemoteException {
        ColoredString.printlnInfo("A client make a request for keyword: "
                + keyword);
        return contents.getContentsFromKeyword(keyword);
    }

    @Override
    public Content uploadContent(String title, String description)
            throws RemoteException {
        Content c = contents.addContent(title, description);
        if (c == null) {
            ColoredString.printlnWarning("Client tried to upload an existing "
                    + "or incorrect content");
        } else {
            ColoredString.printlnCyan("-- INFO: New content has been added --");
            ColoredString.printlnCyan("KEY: " + c.getKey());
            ColoredString.printlnCyan(c.toString());
            ColoredString.printlnCyan("--------------------------------------");
            notifyAllNewContent(c);
        }
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

    private static void notifyAllNewContent(Content c) {
        for (MyTubeCallback callback : callbackObjects) {
            try {
                callback.notifyNewContent(c);
            } catch (RemoteException ex) {
                ColoredString.printlnWarning("Can not notify new content "
                        + "to a client");
            }
        }
    }

    private static void notifyAllServerStopped() {
        for (MyTubeCallback callback : callbackObjects) {
            try {
                callback.notifyServerStopped();
            } catch (RemoteException ex) {

            }
        }
    }

    public void exit() {
        notifyAllServerStopped();
        contents.disconnect();
    }

}
