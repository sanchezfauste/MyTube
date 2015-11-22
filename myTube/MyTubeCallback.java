package myTube;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Class used to notify the addition of new contents to the Clients and that the
 * Server ends his execution
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public interface MyTubeCallback extends Remote {

    /**
     * Notifies the addition of new Content to a Client
     *
     * @param c the Content to be notified
     * @throws RemoteException if the Content can't be notified
     */
    public void notifyNewContent(Content c) throws RemoteException;

    /**
     * Notifies a Client that Server has stopped
     *
     * @throws RemoteException if can't norify that Server has stopped
     */
    public void notifyServerStopped() throws RemoteException;

}
