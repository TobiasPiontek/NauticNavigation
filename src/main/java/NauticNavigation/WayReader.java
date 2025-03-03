package NauticNavigation;

import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

/**
 * This class is used to detect the Node ids of the ways
 */
public class WayReader implements Sink {

    Way myWay;

    @Override
    public void initialize(Map<String, Object> arg0) {
    }

    // gets IDs of all WayNodes with Tag "coastline"
    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            // Nothing to do here
        } else if (entityContainer instanceof WayContainer) {
            myWay = ((WayContainer) entityContainer).getEntity();

            for (Tag myTag : myWay.getTags()) {

                // loop over all Ways with Tag "coastline"
                if ("coastline".equalsIgnoreCase(myTag.getValue())) {

                    // add #Nodes in Way to wayIds
                    if (FileReader.wayIds.isEmpty()) {
                        FileReader.wayIds.add(0);
                    } else {
                        FileReader.wayIds.add(FileReader.nodeIds.size());
                    }

                    // add every Node to nodeIds
                    for (int i = 0; i < myWay.getWayNodes().size(); i++) {
                        FileReader.nodeIds.add(myWay.getWayNodes().get(i).getNodeId());

                    }

                    break;
                }
            }

        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else {

        }
    }

    @Override
    public void complete() {
    }

    @Override
    public void close() {
    }

}