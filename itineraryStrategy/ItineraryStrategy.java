package itineraryStrategy;

import java.util.List;
import mobileAgent.Node;

/**
 * Represents an itinerary strategy for a MobileAgent
 *
 * @author Meritxell Jordana, Marc Sanchez
 */
public interface ItineraryStrategy {

    /**
     * Generates the itinerary of the MobileAgent
     *
     * @param nodes list of nodes to visit
     * @return itinerary that the MobileAgent must follow
     */
    public List<Node> getItinerary(List<Node> nodes);

}
