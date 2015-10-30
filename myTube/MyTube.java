package myTube;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MyTube extends Remote {

    public Content getContentFromKey(int key) throws RemoteException;

    public Content getContentFromTitle(String title) throws RemoteException;

    public List<Content> getContentsFromKeyword(String keyword)
            throws RemoteException;

    public Content uploadContent(String title, String description)
            throws RemoteException;

    public void addCallback(MyTubeCallback callbackObject)
            throws RemoteException;

    public void removeCallback(MyTubeCallback callbackObject)
            throws RemoteException;

}
