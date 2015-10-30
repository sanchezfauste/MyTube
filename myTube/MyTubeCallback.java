package myTube;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyTubeCallback extends Remote {

    public void notifyNewContent(Content c) throws RemoteException;

    public void notifyServerStopped() throws RemoteException;

}
