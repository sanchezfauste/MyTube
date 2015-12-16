package itineraryStrategy;

import java.util.Collections;
import java.util.List;
import mobileAgent.Node;

/**
 * Get an inverse itinerary
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class ReverseStrategy implements ItineraryStrategy {

    @Override
    public List<Node> getItinerary(List<Node> nodes) {
        Collections.reverse(nodes);
        return nodes;
    }

}
