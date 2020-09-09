package MoogleGaps;

import java.util.Scanner;

public class Benckmark {

    private static int[] dijkstraPulls;
    private static long[] dijkstraTimeConsumption;
    private static boolean[] dijkstrafoundWay;

    private static int[] aStarPulls;
    private static long[] aStarTimeConsumption;
    private static boolean[] aStarFoundWay;

    public static void performBenchmark() {
        System.out.println("Setup the benchmark configuration");
        GridGraph.deserialize(CLInterface.getFilename(".ser", "./OSMCacheData"));
        System.out.print("Enter how many routes should be calculated: ");
        Scanner scanner = new Scanner(System.in);
        int sampleSize = scanner.nextInt();
        generateNodePoints(sampleSize);
        int[] startNodes = generateNodePoints(sampleSize);
        int[] endNodes = generateNodePoints(sampleSize);
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
            //System.out.println("Debug: longitude: " + GridGraph.idToLongitude(nodeIdGridGraph) + ", latitude: " + GridGraph.idToLatitude(nodeIdGridGraph));
        }
        return nodesInWater;
    }

    private static void printResults() {
        for (int i = 0; i < dijkstraPulls.length; i++) {
            System.out.println("Dijkstra: " + dijkstraTimeConsumption[i] / Math.pow(10, 9) + "Astar: " + aStarTimeConsumption[i] / Math.pow(10, 9));
        }
    }


}
