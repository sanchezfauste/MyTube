package itineraryStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import mobileAgent.Node;

/**
 * Get a random itinerary
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class RandomStrategy implements ItineraryStrategy {

    @Override
    public List<Node> getItinerary(List<Node> nodes) {
        Collections.shuffle(nodes, new Random(System.nanoTime()));
        return nodes;
    }

}
