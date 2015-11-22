package mobileAgent;

import itineraryStrategy.ItineraryStrategy;
import coloredString.ColoredString;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import myTube.MyTube;

/**
 * MobileAgent of the Application MyTube
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class MobileAgent implements Serializable {

    private final String agentName;
    private final List<Node> nodes;
    private final List<String> visitedHosts;
    private int nextNode;
    private AgentCallback callbackObject;

    /**
     * Creates a MobileAgent
     *
     * @param agentName the name of the MobileAgent
     * @param nodesFile the file containing the nodes that agent has to visit
     * @param strategy the strategy that the agent must follow
     */
    public MobileAgent(String agentName, String nodesFile,
            ItineraryStrategy strategy) {
        this.agentName = agentName;
        this.nodes = strategy.getItinerary(getNodes(nodesFile));
        nextNode = 0;
        visitedHosts = new ArrayList<String>();
        try {
            callbackObject = new AgentCallbackImpl();
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't create callbackAgent");
        }
    }

    private List<Node> getNodes(String nodesFile) {
        List<Node> nodes = new ArrayList<Node>();
        BufferedReader f;
        try {
            f = new BufferedReader(new FileReader(nodesFile));
            try {
                String line;
                while ((line = f.readLine()) != null) {
                    if (line.isEmpty() || line.charAt(0) == '#') {
                        continue;
                    }
                    String[] node = line.split(":");
                    nodes.add(new Node(node[0], Integer.parseInt(node[1]), node[2]));
                }
            } catch (IOException ex) {
                ColoredString.printlnError("Can't read nodes file: " + nodesFile);
            }
            f.close();
        } catch (Exception ex) {
            ColoredString.printlnError("Can't access nodes file: " + nodesFile);
        }
        return nodes;
    }

    private Node getNextNode() {
        if (nextNode < nodes.size()) {
            return nodes.get(nextNode++);
        } else {
            return null;
        }
    }

    /**
     * Executes the MobileAgent functionality
     */
    public void execute() {
        ColoredString.printlnPurple("[" + agentName + "]: Hello!");
        try {
            visitedHosts.add(visitedHosts.size() + " - "
                    + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ex) {
            ColoredString.printlnError("Agent " + agentName
                    + " can't get the hostname");
        }
        while (true) {
            Node n = getNextNode();
            if (n != null) {
                try {
                    Registry registry = LocateRegistry.getRegistry(n.getHost(),
                            n.getPort());
                    MyTube stub = (MyTube) registry.lookup(n.getRegistryName());
                    ColoredString.printlnInfo("Sending agent " + agentName
                            + " to node " + n.getHost() + ":" + n.getPort()
                            + ":" + n.getRegistryName());
                    stub.sendAgent(this);
                } catch (Exception ex) {
                    ColoredString.printlnError("Can't send agent " + agentName
                            + " to node " + n.getHost() + ":" + n.getPort()
                            + ":" + n.getRegistryName());
                    continue;
                }
            } else {
                backHome();
            }
            break;
        }
    }

    private void backHome() {
        try {
            callbackObject.backHome(this);
            ColoredString.printlnPurple("[" + agentName + "]: "
                    + "I'm comming back home!!!");
            ColoredString.printlnInfo("Sending agent " + agentName + " back home");
        } catch (RemoteException ex) {
            ColoredString.printlnError("[" + agentName + "]: I can't go home");
        }
    }

    /**
     * Prints the visited hosts by the MobileAgent
     */
    public void printVisitedHosts() {
        if (visitedHosts.isEmpty()) {
            ColoredString.printlnPurple("Agent has no visited hosts yet!");
        } else {
            ColoredString.printlnPurple("------ Visited hosts by agent ------");
            for (String visitedHost : visitedHosts) {
                ColoredString.printlnPurple(visitedHost);
            }
            ColoredString.printlnPurple("------------------------------------");
        }
    }

    /**
     * Get the name of the MobileAgent
     *
     * @return the name of the MobileAgent
     */
    public String getName() {
        return agentName;
    }

}
