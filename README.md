# MyTube
MyTube RMI Java Application

# Considerations

- The registered name of the application on RMI Registry is `MyTube`.

- The server creates a RMI Registry on the specified port if not exists. If a RMI Registry is already running on that port, the server only registers the name of application.

- The server uses SQLite to manage the contents. It uses JDBC Library that is included on dir `lib`.

- This application is a prototype. 

# Compile the sources

Compile the server `$ javac coloredString/*.java myTube/*.java server/*.java`

Compile the client `$ javac coloredString/*.java myTube/*.java client/*.java`

# Run the server

You can execute a server instance with `$ java -classpath ".:lib/sqlite-jdbc-3.7.2.jar" server.Server <port> [host]`

- The `-classpath ".:lib/sqlite-jdbc-3.7.2.jar"` option is necessary to specify that the server needs JDBC library to run.

- `<port>` It is the port where the server listens for client petitions.

- `[host]` The server waits for client connections on this IP. Default value is `localhost`. If you want to run the client and server on different machines, you must specify the IP that clients will use to connect to the server. Normally your external IP.

- You can stop the server with `CTRL+C`.

# Run the client

You can execute a client instance with `$ java client.Client <host> <port>`

- `<port>` The port where the server listens.

- `<host>` The IP of the server. 