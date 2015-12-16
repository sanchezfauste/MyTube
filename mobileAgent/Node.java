package mobileAgent;

import java.io.Serializable;

/**
 * Represents a Node of the MobileAgent itinerary
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class Node implements Serializable {

    private final String host;
    private final int port;
    private final String registryName;

    /**
     * Creates a Node instance
     *
     * @param host host
     * @param port port number
     * @param registryName registered name on RMI Registry
     */
    public Node(String host, int port, String registryName) {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
    }

    /**
     * Get the name of the Host
     *
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * Get the port
     *
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the registryName
     *
     * @return registryName
     */
    public String getRegistryName() {
        return registryName;
    }

}
