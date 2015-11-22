package itineraryStrategy;

import java.util.List;
import mobileAgent.Node;

/**
 * Get a sequential itinerary
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public class SequentialStrategy implements ItineraryStrategy {

    @Override
    public List<Node> getItinerary(List<Node> nodes) {
        return nodes;
    }

}
