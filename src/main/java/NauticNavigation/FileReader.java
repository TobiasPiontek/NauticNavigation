package NauticNavigation;

import crosby.binary.osmosis.OsmosisReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

public class FileReader {

    //Arrays for the Node management
    public static ArrayList<Long> nodeIds = new ArrayList<Long>();
    //public static ArrayList<Integer> nodeIdLookUp;
    public static Integer[] nodeIdLookUps;

    //Arrays for the ways with their coordinates
    public static List<Integer> wayIds = new ArrayList<Integer>();
    public static double[] longitudes;
    public static double[] latitudes;

    /**
     * Needs to be called to trigger the read in process
     *
     * @param relativeFilePath the Path to the location of a PBF File
     */
    public static void readPbfFile(String relativeFilePath) {
        //Read the PBF file
        InputStream wayInputStream = null;
        InputStream nodeInputStream = null;
        try {
            wayInputStream = new FileInputStream(relativeFilePath);
            nodeInputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        //Trigger the first stage with the read of the way nodes
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Reading ways...");
        OsmosisReader wayReader = new OsmosisReader(wayInputStream);
        WayReader wayData = new WayReader();
        wayReader.setSink(wayData);
        wayReader.run();
        System.out.println(wayIds.size() + " wayNodes detected, " + nodeIds.size() + " nodes detected!");

        System.out.println(new Timestamp(System.currentTimeMillis()) + " Sorting NodeIds...");

        NodeSortComparator comparator = new NodeSortComparator(nodeIds);
        Integer[] nodeIdsInverted = comparator.createIndexArray();
        Arrays.sort(nodeIdsInverted, comparator);

        nodeIdLookUps = new Integer[nodeIdsInverted.length];
        for (int i = 0; i < nodeIdsInverted.length; i++) {
            //inverted.set(index[i],i);
            nodeIdLookUps[nodeIdsInverted[i]] = i;
        }
        nodeIdsInverted = null;     //to tell the garbage collection to delete this value

        Collections.sort(nodeIds);

        //allocating the arrays with their sizes
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Extracting node coordinates...");
        longitudes = new double[nodeIds.size()];
        latitudes = new double[nodeIds.size()];

        // extract longitudes and latitudes
        OsmosisReader nodeReader = new OsmosisReader(nodeInputStream);
        NodeReader nodeData = new NodeReader();
        nodeReader.setSink(nodeData);
        nodeReader.run();
    }

    /**
     * @param wayId the id of the way in the order where they have been read in
     * @return a ArrayList with all nodes included
     */
    public static ArrayList<Long> getWayAtId(int wayId) {
        ArrayList<Long> wayAtIndex = new ArrayList<Long>();
        for (int i = wayIds.get(wayId); i < FileReader.wayIds.get(wayId + 1); i++) {
            wayAtIndex.add(nodeIds.get(i));
        }
        return wayAtIndex;
    }

    /**
     * @param wayIndex the id of the way in the order where they have been read in
     * @return the latitute coordinates in order of the respecting way
     */
    public static double[] getLatitudesOfWay(int wayIndex) {
        int startpoint;
        int endpoint;
        startpoint = wayIds.get(wayIndex);
        if (wayIndex < wayIds.size() - 1) { //if its not the last element
            endpoint = wayIds.get(wayIndex + 1) - 1;
        } else {
            endpoint = nodeIds.size() - 1;
        }
        int size = endpoint - startpoint;
        double[] wayAtId = new double[size];

        for (int i = 0; i < size; i++) {
            wayAtId[i] = latitudes[nodeIdLookUps[i + startpoint]];
        }

        return wayAtId;
    }

    /**
     * @param wayIndex the id of the way in the order where they have been read in
     * @return the longitudes coordinates in order of the respecting way
     */
    public static double[] getLongitudesOfWay(int wayIndex) {
        int startpoint;
        int endpoint;
        startpoint = wayIds.get(wayIndex);
        if (wayIndex < wayIds.size() - 1) { //if its not the last element
            endpoint = wayIds.get(wayIndex + 1) - 1;
        } else {
            endpoint = nodeIds.size() - 1;
        }
        int size = endpoint - startpoint;
        double[] wayAtId = new double[size];

        for (int i = 0; i < size; i++) {
            wayAtId[i] = longitudes[nodeIdLookUps[i + startpoint]];
        }
        return wayAtId;
    }

    /**
     * @param id The id of the way to obtain the first node
     * @return the Node ID of the first node
     */
    public static long getFirstNodeOfWay(int id) {
        return nodeIds.get(nodeIdLookUps[wayIds.get(id)]);
    }

    /**
     * @param id The id of the way to obtain the last node
     * @return the Node ID of the last node
     */
    public static long getLastNodeOfWay(int id) {
        if (id == wayIds.size() - 1) {
            return nodeIds.get(nodeIdLookUps[nodeIds.size() - 1]);
        } else {
            return nodeIds.get(nodeIdLookUps[wayIds.get(id + 1) - 1]);
        }
    }

    /**
     * @param id The id of the way to obtain the length
     * @return The length of the way with this id
     */
    public static int getLengthOfWay(int id) {
        if (id < wayIds.size() - 1) {
            return wayIds.get(id + 1) - 1 - wayIds.get(id);
        } else {
            return (nodeIds.size() - 1) - wayIds.get(id);
        }
    }


    public static class NodeSortComparator implements Comparator<Integer> {
        private final ArrayList<Long> array;

        public NodeSortComparator(ArrayList<Long> array) {
            this.array = array;
        }

        public Integer[] createIndexArray() {
            Integer[] indexes = new Integer[array.size()];
            for (int i = 0; i < array.size(); i++) {
                indexes[i] = i;
            }
            return indexes;
        }

        @Override
        public int compare(Integer index1, Integer index2) {
            return array.get(index1).compareTo(array.get(index2));
        }
    }
}
