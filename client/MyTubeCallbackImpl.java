package client;

import coloredString.ColoredString;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import myTube.Content;
import myTube.MyTubeCallback;

/**
 * Class used to notify the addition of new contents to the Clients and that the
 * Server ends his execution
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class MyTubeCallbackImpl extends UnicastRemoteObject
        implements MyTubeCallback {

    /**
     * Create a MyTubeCallbackImpl
     *
     * @throws RemoteException if can't create a new instance
     */
    public MyTubeCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void notifyNewContent(Content c) throws RemoteException {
        ColoredString.printlnCyan("-- INFO: New content has been added --");
        ColoredString.printlnCyan(c.toString());
        ColoredString.printlnCyan("--------------------------------------");
    }

    @Override
    public void notifyServerStopped() throws RemoteException {
        ColoredString.printlnRed("Server stopped");
        System.exit(0);
    }

}
