package MoogleGaps;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;


public class WebServer {
    private static String navigationModes;

    /**
     * @param navigationMode accepts either "astar" or "dijkstra" as an input to switch between both navigation modes
     */
    public static void startWebServer(String navigationMode) {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Starting web server...");
        InetSocketAddress Adresse = new InetSocketAddress(8004);
        HttpServer server = null;
        try {
            server = HttpServer.create(Adresse, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        navigationModes = navigationMode;
        server.createContext("/NauticNavigation", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Web server is online!");
        System.out.println("Start of the web server communication logs:");

    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "";
            if (t.getRequestMethod().equals("GET")) {
                System.out.println("GET has been sent!");
                response = "get request received!";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

            if (t.getRequestMethod().equals("POST")) {
                String input = "";
                // Block to read Post request
                int x = t.getRequestBody().read();
                while (x != -1) {
                    input = input + (char) x;
                    x = t.getRequestBody().read();
                }

                System.out.println(new Timestamp(System.currentTimeMillis()) + " [Frontend ->  Backend]: " + input);
                String[] split = input.split(";");

                //Procedure to set the startnode
                if (split[0].equals("SetNode")) {
                    String[] latlng = split[1].split(",");
                    double startLatitude = Double.parseDouble(latlng[0]);
                    double startLongitude = Double.parseDouble(latlng[1]);
                    startLongitude = ((((startLongitude + 180) % 360) + 360) % 360) - 180;
                    int nodeID = GridGraph.findVertex(startLongitude, startLatitude);
                    nodeID = GridGraph.findVertexInWater(nodeID);
                    response = GridGraph.idToLatitude(nodeID) + "," + GridGraph.idToLongitude(nodeID);
                }

                if (split[0].equals("calculateRoute")) {
                    String[] startNode = split[1].split(",");
                    String[] endNode = split[2].split(",");
                    double startLatitude = Double.parseDouble(startNode[0]);
                    double startLongitude = Double.parseDouble(startNode[1]);
                    double endLatitude = Double.parseDouble(endNode[0]);
                    double endLongitude = Double.parseDouble(endNode[1]);
                    int sourceId = GridGraph.findVertex(startLongitude, startLatitude);
                    int targetId = GridGraph.findVertex(endLongitude, endLatitude);
                    ArrayList<Integer> route;
                    if (navigationModes.equalsIgnoreCase("dijkstra")) {
                        route = Navigation.dijkstra(sourceId, targetId);
                    } else if (navigationModes.equalsIgnoreCase("astar")) {
                        route = Navigation.aStar(sourceId, targetId);
                    } else {
                        route = new ArrayList();
                        System.err.println("This routing option has not been implemented!");
                    }
                    response = GeoJson.generateRoute(GridGraph.idToLongitude(route), GridGraph.idToLatitude(route));
                }
                System.out.println(new Timestamp(System.currentTimeMillis()) + " [Backend  -> Frontend]: " + response);
                t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
