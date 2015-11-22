package mobileAgent;

import coloredString.ColoredString;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Allows the MobileAgent to go back home
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class AgentCallbackImpl extends UnicastRemoteObject
        implements AgentCallback {

    /**
     * Creates AgentCallbackImpl instance
     *
     * @throws RemoteException if can't create a new instance
     */
    public AgentCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void backHome(MobileAgent agent) throws RemoteException {
        ColoredString.printlnPurple("[" + agent.getName()
                + "]: I'm comming home!");
        agent.printVisitedHosts();
    }

}
