package MoogleGaps;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Benckmark {

    private static int[] dijkstraPulls;
    private static long[] dijkstraTimeConsumption;
    private static boolean[] dijkstrafoundWay;

    private static int[] aStarPulls;
    private static long[] aStarTimeConsumption;
    private static boolean[] aStarFoundWay;

    private static int[] startNodes;
    private static int[] endNodes;

    public static void performBenchmark() {
        System.out.println("Setup the benchmark configuration");
        GridGraph.deserialize(CLInterface.getFilename(".ser", "./OSMCacheData"));
        System.out.print("Enter how many routes should be calculated: ");
        Scanner scanner = new Scanner(System.in);
        int sampleSize = scanner.nextInt();
        generateNodePoints(sampleSize);
        startNodes = generateNodePoints(sampleSize).clone();

        endNodes = generateNodePoints(sampleSize);
        //init Benchmarking variables
        dijkstraPulls = new int[sampleSize];
        dijkstraTimeConsumption = new long[sampleSize];
        dijkstrafoundWay = new boolean[sampleSize];

        aStarPulls = new int[sampleSize];
        aStarTimeConsumption = new long[sampleSize];
        aStarFoundWay = new boolean[sampleSize];

        calculateDijkstraBenchmark(startNodes, endNodes);
        calculateAStarBenchmark(startNodes, endNodes);
        printResults();
    }

    private static void calculateDijkstraBenchmark(int[] startNodes, int[] endNodes) {
        for (int i = 0; i < startNodes.length; i++) {
            long startTime = System.nanoTime();
            dijkstrafoundWay[i] = !Navigation.dijkstra(startNodes[i], endNodes[i]).isEmpty();
            long endTime = System.nanoTime();
            dijkstraTimeConsumption[i] = endTime - startTime;
            dijkstraPulls[i] = Navigation.pulls;
        }
    }

    private static void calculateAStarBenchmark(int[] startNodes, int[] endNodes) {
        for (int i = 0; i < startNodes.length; i++) {
            long startTime = System.nanoTime();
            aStarFoundWay[i] = !Navigation.aStar(startNodes[i], endNodes[i]).isEmpty();
            long endTime = System.nanoTime();
            aStarTimeConsumption[i] = endTime - startTime;
            aStarPulls[i] = Navigation.pulls;
        }
    }


    private static int[] generateNodePoints(int sampleSize) {
        int[] nodesInWater = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            int nodeIdGridGraph;
            do {
                double latitude = Math.random() * 360 - 180;
                double longitude = Math.random() * 180 - 90;
                nodeIdGridGraph = GridGraph.findVertex(longitude, latitude);
            } while (!Navigation.isSurroundedByWater(nodeIdGridGraph));
            nodesInWater[i] = nodeIdGridGraph;
            //System.out.println("Debug: longitude: " + GridGraph.idToLongitude(nodeIdGridGraph) + ", latitude: " + GridGraph.idToLatitude(nodeIdGridGraph));
        }
        return nodesInWater.clone();
    }

    private static void printResults() {
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < dijkstraPulls.length; i++) {
            System.out.println("from: " + "lat: " + df.format(GridGraph.idToLatitude(startNodes[i])) + " lon: " + df.format(GridGraph.idToLongitude(startNodes[i]))
                    + " to " + "lat: " + df.format(GridGraph.idToLatitude(endNodes[i])) + " lon: " + df.format(GridGraph.idToLongitude(endNodes[i]))
                    + "Dijkstra: " + dijkstraTimeConsumption[i] / Math.pow(10, 9) + "sec " + " Nodes popped: " + dijkstraPulls[i]
                    + "Astar: " + aStarTimeConsumption[i] / Math.pow(10, 9) + "sec" + " Nodes popped: " + aStarPulls[i]);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM;HH.mm");
                writeDataToCsv("./BenchmarkData/" + dijkstraPulls.length + "runs," + "grid_size=" + GridGraph.vertexData.length + ",date=" + sdf.format(new Timestamp(System.currentTimeMillis())) + ".csv");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeDataToCsv(String filepath) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filepath);
        out.println("Das,ist,ein,test");
        out.close();
    }
}
