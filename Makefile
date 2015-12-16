JAVAC=javac
.SUFFIXES: .java .class

serverSources = $(wildcard server/Server.java)
serverClasses = $(serverSources:.java=.class)
clientSources = $(wildcard client/Client.java)
clientClasses = $(clientSources:.java=.class)

all: client server

client: $(clientClasses)

server: $(serverClasses)

clean:
	rm -rf myTube/*.class \
		coloredString/*.class \
		server/*.class \
		client/*.class \
		itineraryStrategy/*.class \
		mobileAgent/*.class \
		org/json/simple/*.class \
		org/json/simple/parser/*.class \
		Server.jar \
		Client.jar

dist: server client
	jar cmf META-INF/CLIENTMANIFEST.MF Client.jar \
		myTube/*.class \
		coloredString/*.class \
		client/*.class \
		itineraryStrategy/*.class \
		mobileAgent/*.class \
		org/json/simple/*.class \
		org/json/simple/parser/*.class \
		lib/sqlite-jdbc-3.7.2.jar

	jar cmf META-INF/SERVERMANIFEST.MF Server.jar \
		myTube/*.class \
		coloredString/*.class \
		server/*.class \
		itineraryStrategy/*.class \
		mobileAgent/*.class \
		org/json/simple/*.class \
		org/json/simple/parser/*.class \
		lib/sqlite-jdbc-3.7.2.jar

%.class: %.java
	$(JAVAC) $<
