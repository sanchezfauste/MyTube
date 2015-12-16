package client;

import coloredString.ColoredString;
import itineraryStrategy.ItineraryStrategy;
import itineraryStrategy.RandomStrategy;
import itineraryStrategy.ReverseStrategy;
import itineraryStrategy.SequentialStrategy;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.io.IOException;
import myTube.Content;
import mobileAgent.MobileAgent;
import myTube.MyTube;
import myTube.MyTubeCallback;

/**
 * Represents a Client of MyTube Application
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class Client {
    
    private MyTube stub;
    private Registry registry;
    private MyTubeCallback callbackObject;
    private final String host;
    private final int port;
    private final String registryName;
    private final String registryURL;

    /**
     * Creates a Client object
     *
     * @param host host of the Server
     * @param port port of the Server
     * @param registryName name of the registered service on RMI Registry
     */
    public Client(String host, int port, String registryName) {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
        registryURL = "rmi://" + host + ":" + port
                + "/" + registryName;
    }

    /**
     * Connects the Client to the specified Server
     */
    public void connectToTheServer() {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            registry = LocateRegistry.getRegistry(host, port);
            stub = (MyTube) registry.lookup(registryName);
            callbackObject = new MyTubeCallbackImpl();
            stub.addCallback(callbackObject);
            ColoredString.printlnSuccess("MyTube client connected on: "
                    + registryURL);
        } catch (Exception ex) {
            ColoredString.printlnError("Can not connect to the server");
            System.exit(1);
        }
    }

    /**
     * Disconnects the Client from the Server
     */
    public void disconnectFromTheServer() {
        try {
            stub.removeCallback(callbackObject);
        } catch (Exception ex) {
            ColoredString.printlnError("Error disconnecting from the server");
        }
    }

    /**
     * Get a Content from key or null if not exist
     *
     * @param key key of the Content
     * @return Content with specified key
     */
    public Content getContentFromKey(String key) {
        try {
            return stub.getContentFromKey(key);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get content from server");
            return null;
        }
    }

    /**
     * Get a Content from matching title or null if not exist
     *
     * @param title title of the Content
     * @return Content matching title
     */
    public Content getContentFromTitle(String title) {
        try {
            return stub.getContentFromTitle(title);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get content from server");
            return null;
        }
    }

    /**
     * Get all Contents with a title that matches a keyword
     *
     * @param keyword keyword used to search Contents
     * @return list of matching Contents
     */
    public List<Content> getContentsFromKeyword(String keyword) {
        try {
            return stub.getContentsFromKeyword(keyword);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't get contents from server");
            return null;
        }
    }

    /**
     * Upload a new content to the Server and returns a Content object or null
     * if the Content can't be added
     *
     * @param title title of the Content
     * @param description description of the Content
     * @return the added Content
     */
    public Content uploadContent(String title, String description) {
        try {
            return stub.uploadContent(title, description);
        } catch (RemoteException ex) {
            ColoredString.printlnError("Can't upload content to server");
            return null;
        }
    }

    /**
     * Upload the media file of registered content
     *
     * @param c The content to upload
     * @return true if the Content file is uploaded correctly
     * @throws RemoteException if can't make the petition to the Server
     */
    public boolean uploadContentFile(Content c) throws RemoteException {
        return stub.uploadContentFile(c);
    }
    
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.err.println("Parameters: <host> <port> "
                    + "[registryName] [nodesFile]");
            System.exit(1);
        }
        
        String registryName = (args.length < 3) ? "MyTube" : args[2];
        String nodesFile = (args.length < 4) ? "nodes.data" : args[3];
        
        final Client client = new Client(args[0], Integer.parseInt(args[1]),
                registryName);
        
        client.connectToTheServer();
        
        System.out.println("Choose one of the following options:");
        
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            
            System.out.println("Press 1 to search a digital content by key");
            System.out.println("Press 2 to search a digital content by title");
            System.out.println("Press 3 to upload new digital content with its description");
            System.out.println("Press 4 to search matching contents with the title");
            System.out.println("Press 5 to exit");
            System.out.println("Press 6 to run the agent");
            
            try {
                
                int option = Integer.parseInt(sc.readLine());
                
                if (option == 1) {
                    System.out.print("Enter the key you want to search: ");
                    Content c = client.getContentFromKey(sc.readLine());
                    if (c == null) {
                        ColoredString.printlnWarning("This key doesn't exist or is unavaliable!");
                    } else {
                        c.writeContent();
                        ColoredString.printlnSuccess("Key found!");
                        ColoredString.printlnCyan("--------------------------------------");
                        ColoredString.printlnCyan(c.toString());
                        ColoredString.printlnCyan("--------------------------------------");
                    }
                    
                } else if (option == 2) {
                    System.out.println("Enter the title you want to search:");
                    Content c = client.getContentFromTitle(sc.readLine());
                    if (c == null) {
                        ColoredString.printlnWarning("This title doesn't exist or is unavaliable!");
                    } else {
                        c.writeContent();
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
                    System.out.println("Enter the path of media content:");
                    String contentFile = sc.readLine();
                    Content c = client.uploadContent(title, description);
                    if (c == null) {
                        ColoredString.printlnWarning("This content already exists or is incorrect!");
                    } else {
                        if (c.loadContent(contentFile) && client.uploadContentFile(c)) {
                            ColoredString.printlnSuccess("Your content was uploaded with key: " + c.getKey());
                        } else {
                            ColoredString.printlnError("Error sending media file to the server");
                        }
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
                    
                } else if (option == 6) {
                    System.out.println("Enter the name of the Agent:");
                    String agentName = sc.readLine();
                    System.out.println("What strategy do you want to use?");
                    System.out.println("1 - Sequential Itinerary");
                    System.out.println("2 - Random Itinerary");
                    System.out.println("3 - Reverse Itinerary");
                    ItineraryStrategy strategy;
                    switch (Integer.parseInt(sc.readLine())) {
                        case 2:
                            strategy = new RandomStrategy();
                            break;
                        case 3:
                            strategy = new ReverseStrategy();
                            break;
                        default:
                            strategy = new SequentialStrategy();
                            break;
                    }
                    ColoredString.printlnPurple("[CLIENT]: Goodbye agent "
                            + agentName);
                    new MobileAgent(agentName, nodesFile, strategy).execute();
                    ColoredString.printlnPurple("[CLIENT]: Congratulations agent "
                            + agentName);
                    
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
