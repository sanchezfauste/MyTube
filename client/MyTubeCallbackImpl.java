package client;

import coloredString.ColoredString;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import myTube.Content;
import myTube.MyTubeCallback;

public class MyTubeCallbackImpl extends UnicastRemoteObject
        implements MyTubeCallback {

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
