package MoogleGaps;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Benckmark {

    private static int[] dijkstraPulls;
    private static long[] dijkstraTimeConsumption;
    private static boolean[] dijkstraFoundWay;

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
        dijkstraFoundWay = new boolean[sampleSize];

        aStarPulls = new int[sampleSize];
        aStarTimeConsumption = new long[sampleSize];
        aStarFoundWay = new boolean[sampleSize];

        calculateDijkstraBenchmark(startNodes, endNodes);
        calculateAStarBenchmark(startNodes, endNodes);
        //printResults();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM;HH.mm");
        try {
            writeDataToCsv("./BenchmarkData/" + dijkstraPulls.length + "runs," + "grid_size=" + GridGraph.vertexData.length + ",date=" + sdf.format(new Timestamp(System.currentTimeMillis())) + ".csv");
        } catch (FileNotFoundException e) {

        }
    }

    private static void calculateDijkstraBenchmark(int[] startNodes, int[] endNodes) {
        for (int i = 0; i < startNodes.length; i++) {
            long startTime = System.nanoTime();
            dijkstraFoundWay[i] = !Navigation.dijkstra(startNodes[i], endNodes[i]).isEmpty();
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
                double latitude = Math.random() * 170.0 - 80.0;
                double longitude = Math.random() * 360.0 - 180.0;
                nodeIdGridGraph = GridGraph.findVertex(longitude, latitude);
            } while (GridGraph.vertexData[nodeIdGridGraph]);
            nodesInWater[i] = nodeIdGridGraph;
        }
        return nodesInWater.clone();
    }


    private static void writeDataToCsv(String filepath) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filepath);
        out.println("start lat, start long, dest lat, dest long, dijkstra time, astar time, dijsktra node pulls,  astar node pulls, dijkstra found, astar found, index");
        double dijkstraTimeTotal = 0;
        double dijkstraTimeFound = 0;
        double aStarTimeTotal = 0;
        double aStarTimeFound = 0;
        long dijkstraPullsTotal = 0;
        long dijkstraPullsFound = 0;
        long aStarPullsTotal = 0;
        long aStarPullsFound = 0;
        int dijkstraFound = 0;
        int aStarFound = 0;
        
        for (int i = 0; i < dijkstraPulls.length; i++) {
            out.println(
                    GridGraph.idToLatitude(startNodes[i]) + ","
                            + GridGraph.idToLongitude(startNodes[i]) + ","
                            + GridGraph.idToLatitude(endNodes[i]) + ","
                            + GridGraph.idToLongitude(endNodes[i]) + ","
                            + dijkstraTimeConsumption[i] / Math.pow(10, 9) + ","
                            + aStarTimeConsumption[i] / Math.pow(10, 9) + ","
                            + dijkstraPulls[i] + ","
                            + aStarPulls[i] + ","
                            + dijkstraFoundWay[i] + ","
                            + aStarFoundWay[i] + ","
                            + i);

            dijkstraTimeTotal += dijkstraTimeConsumption[i] / Math.pow(10, 9);
            aStarTimeTotal += aStarTimeConsumption[i] / Math.pow(10, 9);
            dijkstraPullsTotal += dijkstraPulls[i];
            aStarPullsTotal += aStarPulls[i];

            //calculate only routes that have a path
            if (dijkstraFoundWay[i]) {
                dijkstraTimeFound += dijkstraTimeConsumption[i] / Math.pow(10, 9);
                dijkstraPullsFound += dijkstraPulls[i];
                dijkstraFound++;
            }
            if (aStarFoundWay[i]) {
                aStarTimeFound += aStarTimeConsumption[i] / Math.pow(10, 9);
                aStarPullsFound += aStarPulls[i];
                aStarFound++;
            }
        }
        out.println();
        out.println("Analysis");
        out.println(",Time speedup, Nodes used less,,t dijkstra avg.,t astar avg., total dijkstra, total astar, dijkstra routes, astar routes");
        out.println("Average all" + ","
                + dijkstraTimeTotal / aStarTimeTotal + ","
                + (double) aStarPullsTotal / dijkstraPullsTotal + "," + ","
                + dijkstraTimeTotal / dijkstraPulls.length + ","
                + aStarTimeTotal / dijkstraPulls.length + ","
                + dijkstraPullsTotal / dijkstraPulls.length + ","
                + aStarPullsTotal / dijkstraPulls.length + ","
                + dijkstraPulls.length + ","
                + dijkstraPulls.length + ",");

        out.println();
        out.println("Average found" + ","
                + dijkstraTimeFound / aStarTimeFound + ","
                + (double) aStarPullsFound / dijkstraPullsFound + "," + ","
                + dijkstraTimeFound / dijkstraFound + ","
                + aStarTimeFound / aStarFound + ","
                + dijkstraPullsFound / dijkstraFound + ","
                + aStarPullsFound / aStarFound + ","
                + dijkstraFound + ","
                + aStarFound
        );
        out.println();
        out.close();
    }
}
