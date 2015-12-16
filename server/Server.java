package server;

import coloredString.ColoredString;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import mobileAgent.Node;

/**
 * Represents a Server of MyTube Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class Server {

    private MyTubeImpl stub;
    private Registry registry;
    private final String host;
    private final int port;
    private final String registryName;
    private final String registryURL;
    private final String dbName;
    private final String wsConfigFile;

    /**
     * Creates a Server instance
     *
     * @param host the server waits for client connections on this IP
     * @param port port where the server listens for client petitions
     * @param registryName name of the registered service on RMI Registry
     * @param dbName name of sqlite database file
     * @param wsConfigFile config file of web service
     */
    public Server(String host, int port, String registryName,
            String dbName, String wsConfigFile) {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
        registryURL = "rmi://" + host + ":" + port
                + "/" + registryName;
        this.dbName = dbName;
        this.wsConfigFile = wsConfigFile;
    }

    public static void main(String args[]) {

        if (args.length < 2) {
            System.err.println("Parameters: <host> <port> "
                    + "[registryName] [dbName] [wsConfigFile]");
            System.exit(1);
        }

        String registryName = (args.length < 3) ? "MyTube" : args[2];
        String dbName = (args.length < 4) ? "contents.sqlite" : args[3];
        String wsConfigFile = (args.length < 5) ? "ws.data" : args[4];

        final Server s = new Server(args[0], Integer.parseInt(args[1]),
                registryName, dbName, wsConfigFile);

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void start() {
                ColoredString.printlnRed("Stopping server!");
                try {
                    mainThread.join();
                } catch (Exception e) {
                    ColoredString.printlnError("Can not finalize main process");
                }
                s.stopServer();
                System.exit(0);
            }
        });

        s.runServer();

    }

    /**
     * Runs the Server
     */
    public void runServer() {
        try {
            System.setProperty("java.rmi.server.hostname", host);
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            stub = new MyTubeImpl(dbName, new Node(host, port, registryName),
                    wsConfigFile);
            registry = getRegistry();
            registry.rebind(registryName, stub);
            ColoredString.printlnInfo("MyTube Server ready on: " + registryURL);
        } catch (Exception ex) {
            ColoredString.printlnError("Server error: " + ex.toString());
        }
    }

    /**
     * Stopps the Server
     */
    public void stopServer() {
        if (stub != null) {
            stub.exit();
        }
        try {
            registry.unbind(registryName);
            UnicastRemoteObject.unexportObject(stub, true);
            ColoredString.printlnSuccess("Server stopped correctly");
        } catch (Exception ex) {
            ColoredString.printlnError("Server failed on stop");
        }
    }

    private Registry getRegistry() throws RemoteException {
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(port);
            ColoredString.printlnInfo("RMI registry created at port " + port);
        } catch (RemoteException ex) {
            reg = LocateRegistry.getRegistry(host, port);
            ColoredString.printlnInfo("RMI registry is already created on port "
                    + port);
        }
        return reg;
    }
}
