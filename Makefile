JAVAC=javac
.SUFFIXES: .java .class

myTubeSources = $(wildcard myTube/*.java)
myTubeClasses = $(myTubeSources:.java=.class)
coloredStringSources = $(wildcard coloredString/*.java)
coloredStringClasses = $(coloredStringSources:.java=.class)
serverSources = $(wildcard server/*.java)
serverClasses = $(serverSources:.java=.class)
clientSources = $(wildcard client/*.java)
clientClasses = $(clientSources:.java=.class)
itineraryStrategySources = $(wildcard itineraryStrategy/*.java)
itineraryStrategyClasses = $(itineraryStrategySources:.java=.class)
mobileAgentSources = $(wildcard mobileAgents/*.java)
mobileAgentClasses = $(mobileAgentSources:.java=.class)

all: client server

myTube: $(myTubeClasses)

coloredString: $(coloredStringClasses)

client: myTube coloredString itineraryStrategy mobileAgent $(clientClasses)

server: myTube coloredString itineraryStrategy mobileAgent $(serverClasses)

itineraryStrategy: $(itineraryStrategyClasses)

mobileAgent: $(mobileAgentClasses)

clean:
	rm -rf */*.class Server.jar Client.jar

dist: server client
	jar cmf META-INF/CLIENTMANIFEST.MF Client.jar \
		myTube/*.class \
		coloredString/*.class \
		client/*.class \
		itineraryStrategy/*.class \
		mobileAgent/*.class \
		lib/sqlite-jdbc-3.7.2.jar

	jar cmf META-INF/SERVERMANIFEST.MF Server.jar \
		myTube/*.class \
		coloredString/*.class \
		server/*.class \
		itineraryStrategy/*.class \
		mobileAgent/*.class \
		lib/sqlite-jdbc-3.7.2.jar

%.class: %.java
	$(JAVAC) $<
