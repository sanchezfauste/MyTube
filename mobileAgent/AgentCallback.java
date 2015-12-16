package mobileAgent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Allows the MobileAgent to go back home
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public interface AgentCallback extends Remote {

    /**
     * This function is called when the MobileAgent finishes his itinerary
     *
     * @param agent the MobileAgent who wants to back home
     * @throws RemoteException if the MobileAgent can't go back home
     */
    public void backHome(MobileAgent agent) throws RemoteException;

}
