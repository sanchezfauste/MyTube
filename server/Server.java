package server;

import coloredString.ColoredString;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private MyTubeImpl stub;
    private Registry registry;
    private final String host;
    private final int port;
    private final String registryName;

    public Server(String host, int port) {
        this(host, port, "MyTube");
    }

    public Server(String host, int port, String registryName) {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
    }

    public static void main(String args[]) {

        if (args.length < 1) {
            System.err.println("Parameters: <port> + [host]");
            System.exit(1);
        }

        final Server s = new Server((args.length < 2) ? "localhost" : args[1],
                Integer.parseInt(args[0]));

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

    public void runServer() {
        try {
            System.setProperty("java.rmi.server.hostname", host);
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            stub = new MyTubeImpl();
            registry = getRegistry();
            String registryURL = "rmi://" + host + ":" + port
                    + "/" + registryName;
            registry.rebind(registryName, stub);
            ColoredString.printlnInfo("MyTube Server ready on: " + registryURL);
        } catch (Exception ex) {
            ColoredString.printlnError("Server error: " + ex.toString());
        }
    }

    public void stopServer() {
        if (stub != null) {
            stub.exit();
        }
        try {
            registry.unbind(registryName);
            UnicastRemoteObject.unexportObject(stub, true);
            ColoredString.printlnSuccess("Server stopped correctly");
        } catch (RemoteException | NotBoundException ex) {
            ColoredString.printlnError("Server failed on unbind: "
                    + ex.toString());
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
