# MyTube
MyTube RMI Java Application

# Considerations

- The server creates a RMI Registry on the specified port if not exists. If a RMI Registry is already running on that port, the server only registers the name of application.

- The server uses SQLite to manage the contents. It uses JDBC Library that is included on dir `lib`.

- This application is a prototype.

- If you want to run the client and server on different machines, you must specify the externally IP on client and server. Don't use `localhost`.

- You can't run different servers using the same sqlite database. Please, use different databases.

- You can run different servers at the same machine and port using different registryNames.

# Makefile

You can compile the sources using the Makefile:

- `make all` Compile all the sources

- `make client` Compile the client sources

- `make server` Compile the server sources

- `make dist` Packs all the sources and libraries on a JAR file. Two JARs wills be created: `Server.jar` for the server and `Client.jar` for the client

- `make clean` Clean all the `.class` files, `Server.jar` and `Client.jar`

# Run the server using the Makefile

You can run the server using Makefile

- Execute `make dist` to create `Server.jar`

- Run the server with `java -jar Server.jar <host> <port> [registryName] [dbName] [wsConfigFile]`

    - `<host>` The server waits for client connections on this IP. If you want to run the client and server on different machines, you must specify the IP that clients will use to connect to the server. Normally your external IP.

    - `<port>` It is the port where the server listens for client petitions.

    - `[registryName]` The registered name of the application on RMI Registry. Default value is `MyTube`.

    - `[dbName]` Name of the sqlite database file. Default value is `contents.sqlite`.

    - `[wsConfigFile]` The containing file of the web service config file. Default value is `ws.data`

# Run the client using the Makefile

You can run the server using Makefile

- Execute `make dist` to create `Client.jar`

- Run the client with `java -jar Client.jar <host> <port> [registryName] [nodesFile]`

    - `<host>` The IP of the server.

    - `<port>` The port where the server listens.

    - `[registryName]` The registered name of the application on RMI Registry. Default value is `MyTube`.

    - `[nodesFile]` The file containing the nodes that agent has to visit. Default value is `nodes.data`.

# Compile the sources without Makefile

Compile the server `$ javac coloredString/*.java myTube/*.java server/*.java mobileAgent/*.java itineraryStrategy/*.java org/json/simple/*.java org/json/simple/parser/*.java`

Compile the client `$ javac coloredString/*.java myTube/*.java client/*.java mobileAgent/*.java itineraryStrategy/*.java`

# Run the server without Makefile

You can execute a server instance with `$ java -classpath ".:lib/sqlite-jdbc-3.7.2.jar" server.Server <host> <port> [registryName] [dbName] [wsConfigFile]`

- The `-classpath ".:lib/sqlite-jdbc-3.7.2.jar"` option is necessary to specify that the server needs JDBC library to run.

- `<host>` The server waits for client connections on this IP. If you want to run the client and server on different machines, you must specify the IP that clients will use to connect to the server. Normally your external IP.

- `<port>` It is the port where the server listens for client petitions.

- `[registryName]` The registered name of the application on RMI Registry. Default value is `MyTube`.

- `[dbName]` Name of the sqlite database file. Default value is `contents.sqlite`.

- `[wsConfigFile]` The containing file of the web service config file. Default value is `ws.data`

- You can stop the server with `CTRL+C`.

# Run the client without Makefile

You can execute a client instance with `$ java client.Client <host> <port> [registryName] [nodesFile]`

- `<host>` The IP of the server.

- `<port>` The port where the server listens.

- `[registryName]` The registered name of the application on RMI Registry. Default value is `MyTube`.

- `[nodesFile]` The file containing the nodes that agent has to visit. Default value is `nodes.data`.

# MobileAgent

The client creates the MobileAgent who visits the specified nodes (servers).

To run the MobileAgent you have to specify the nodes to visit (servers). Create a file following the format `host:port:registryName`. You can add comments with `#`.

Example of `nodes.data` file:
```
# host:port:registryName

172.16.123.59:1025:MyTube
172.16.104.246:1025:MyTube
172.16.0.23:1025:MyTube
172.16.0.24:1025:MyTube
172.16.2.138:1025:MyTube
172.16.2.139:1025:MyTube
```

# Web Service Config file

You have to specify the main url of Web Service. Create a file and add the line `http://direction.tld`. You can add comments with `#`. IMPORTANT: Don't add a `/` at the end of the url.

Example of `ws.data` file:
```
# Web Service url

http://mytube-ws.appspot.com
```
