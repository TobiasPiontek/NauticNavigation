package MoogleGaps;


import java.io.File;

public class Main {

    public static void main(String[] args) {
        CLInterface.printTitelScreen();
        int decision = CLInterface.generateNewGridGraph();
        if (decision == 0) {
            System.out.println("Generate grid graph selected!");
            String filepath = CLInterface.getFilename(".pbf", "./OSMMapData");
            int gridGraphSize = CLInterface.enterGridGraphResolution();
            FileReader.readPbfFile(filepath);
            Polygons.createPolygons();
            GridGraph.generate(gridGraphSize);

            File f = new File(filepath);
            GridGraph.serialize(f.getName());
        } else if (decision == 1) {
            System.out.println("Use pre generated grid graph selected!");
            GridGraph.deserialize(CLInterface.getFilename(".ser", "./OSMCacheData"));
        } else if (decision == 2) {
            System.out.println("Benchmarking mode selected!");
            Benckmark.performBenchmark();
        }
        if (decision == 0 || decision == 1) {
            WebServer.startWebServer("astar");
        }
        //For testing purposes
        //GeoJson.printGridGraph()
        //CLInterface.generateNavigationRoute();

        //Start of the Webserver

    }
}
