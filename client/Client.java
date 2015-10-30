package client;

import coloredString.ColoredString;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.io.IOException;
import java.rmi.NotBoundException;
import myTube.Content;
import myTube.MyTube;
import myTube.MyTubeCallback;

public class Client {

    private MyTube stub;
    private Registry registry;
    private MyTubeCallback callbackObject;
    private final String host;
    private final int port;
    private final String registryName;

    public Client(String host, int port) {
        this(host, port, "MyTube");
    }

    public Client(String host, int port, String registryName) {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
    }

    public void connectToTheServer() {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            registry = LocateRegistry.getRegistry(host, port);
            stub = (MyTube) registry.lookup(registryName);
            String registryURL = "rmi://" + host + ":" + port
                    + "/" + registryName;
            callbackObject = new MyTubeCallbackImpl();
            stub.addCallback(callbackObject);
            ColoredString.printlnSuccess("MyTube client connected on: "
                    + registryURL);
        } catch (RemoteException | NotBoundException ex) {
            ColoredString.printlnError("Can not connect to the server");
            System.exit(1);
        }
    }

    public void disconnectFromTheServer() {
        try {
            stub.removeCallback(callbackObject);
        } catch (Exception ex) {
            ColoredString.printlnError("Error disconnecting from the server");
        }
    }

    public Content getContentFromKey(int key) {
        try {
            return stub.getContentFromKey(key);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get content from server");
            return null;
        }
    }

    public Content getContentFromTitle(String title) {
        try {
            return stub.getContentFromTitle(title);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get content from server");
            return null;
        }
    }

    public List<Content> getContentsFromKeyword(String keyword) {
        try {
            return stub.getContentsFromKeyword(keyword);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get contents from server");
            return null;
        }
    }

    public Content uploadContent(String title, String description) {
        try {
            return stub.uploadContent(title, description);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't upload content to server");
            return null;
        }
    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Parameters: <host> + <port>");
            System.exit(1);
        }

        final Client client = new Client(args[0], Integer.parseInt(args[1]));

        client.connectToTheServer();

        System.out.println("Choose one of the following options:");

        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            System.out.println("Press 1 to search a digital content by key");
            System.out.println("Press 2 to search a digital content by title");
            System.out.println("Press 3 to upload new digital content with its description");
            System.out.println("Press 4 to search matching contents with the title");
            System.out.println("Press 5 to exit");

            try {

                int option = Integer.parseInt(sc.readLine());

                if (option == 1) {
                    System.out.print("Enter the key you want to search: ");
                    Content c = client.getContentFromKey(Integer.parseInt(sc.readLine()));
                    if (c == null) {
                        ColoredString.printlnWarning("This key doesn't exist!");
                    } else {
                        ColoredString.printlnSuccess("Key found!");
                        ColoredString.printlnCyan("--------------------------------------");
                        ColoredString.printlnCyan(c.toString());
                        ColoredString.printlnCyan("--------------------------------------");
                    }

                } else if (option == 2) {
                    System.out.println("Enter the title you want to search:");
                    Content c = client.getContentFromTitle(sc.readLine());
                    if (c == null) {
                        ColoredString.printlnWarning("This title doesn't exist!");
                    } else {
                        ColoredString.printlnSuccess("Title found!");
                        ColoredString.printlnCyan("--------------------------------------");
                        ColoredString.printlnCyan(c.toString());
                        ColoredString.printlnCyan("--------------------------------------");
                    }

                } else if (option == 3) {
                    System.out.println("Enter the title of the content you want to upload:");
                    String title = sc.readLine();
                    System.out.println("Enter the description of the content you want to upload:");
                    String description = sc.readLine();
                    Content c = client.uploadContent(title, description);
                    if (c == null) {
                        ColoredString.printlnWarning("This content already exists or is incorrect!");
                    } else {
                        ColoredString.printlnSuccess("Your content was uploaded with key: " + c.getKey());
                    }

                } else if (option == 4) {
                    System.out.println("Enter the title you want to search:");
                    List<Content> contents = client.getContentsFromKeyword(sc.readLine());
                    if (contents.isEmpty()) {
                        ColoredString.printlnWarning("No match coincidences!");
                    } else {
                        ColoredString.printlnSuccess(contents.size() + " title(s) found!");
                        for (Content c : contents) {
                            ColoredString.printlnCyan("--------------------------------------");
                            ColoredString.printlnCyan(c.toString());
                            ColoredString.printlnCyan("--------------------------------------");
                        }
                    }

                } else if (option == 5) {
                    client.disconnectFromTheServer();
                    ColoredString.printlnSuccess("You have been disconnected from the server");
                    System.out.println("Thanks to use MyTube. See you soon!");
                    try {
                        sc.close();
                    } catch (IOException ex) {
                        ColoredString.printlnError("BufferedReader can't be closed");
                    }
                    System.exit(0);

                } else {
                    ColoredString.printlnWarning("Wrong number of option entered");
                    System.out.println("Try it again");
                }

                System.out.println("\nWhat do you want to do now? ");

            } catch (IOException ex) {
                ColoredString.printlnError("Some error");
                System.exit(1);
            }
        }
    }
}
