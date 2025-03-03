package NauticNavigation;

import java.util.ArrayList;

public class GeoJson {

    // print GeoJSON of nodeIds
    public static void DebugPrintGeoJson() {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        int elementsToPrint = 100;
        for (int i = 0; i < elementsToPrint; i++) {       // used before: FileReader.nodeIds.size()
            System.out.println(getGeoJsonElement(6, FileReader.longitudes[i], FileReader.latitudes[i]));

            if (i != elementsToPrint - 1) {
                System.out.print("}, ");
            } else {
                System.out.println("}]");
                System.out.println("}");
            }
        }
    }

    /**
     * internal helper method
     *
     * @param moveCharsIn the static amount of Chars to move in
     * @param longitude   longitude coordinate of node to display
     * @param latitude    latitude coordinate of node to display
     * @return a GeoJson representation of the coordinates
     */
    private static String getGeoJsonElement(int moveCharsIn, double longitude, double latitude) {
        String indention = "";
        for (int i = 0; i < moveCharsIn; i++) {
            indention.concat(" ");
        }
        String Element = "{\n" +
                indention + "\"type\": \"Feature\",\n" +
                indention + "\"geometry\": {\n" +
                indention + "  \"type\": \"Point\",\n" +
                indention + "  \"coordinates\": [" + longitude + ", " + latitude + "]\n" +
                indention + "},\n" +
                indention + "\"properties\": {\n" +
                indention + "\"prop0\": \"value0\"\n" +
                indention + "}\n";
        return Element;
    }

    /**
     * Simple debug method, can pring a variety of coordinates as dots
     *
     * @param longitudes
     * @param latitudes
     */
    public static void DebugprintNodes(double[] longitudes, double[] latitudes) {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        for (int i = 0; i < longitudes.length; i++) {       // used before: FileReader.nodeIds.size()
            System.out.println(getGeoJsonElement(6, longitudes[i], latitudes[i]));

            if (i != longitudes.length - 1) {
                System.out.print("}, ");
            } else {
                System.out.println("}]");
                System.out.println("}");
            }
        }
    }

    /**
     * This function prints out poligons
     *
     * @param longitudes list of longitude coordinates
     * @param latitudes  list of latitude coordinates
     */
    public static void DebugPrintPolygon(double[] longitudes, double[] latitudes) {

        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        System.out.println("    {");
        System.out.println("      \"type\": \"Feature\",");
        System.out.println("      \"properties\": {},");
        System.out.println("      \"geometry\": {");
        System.out.println("        \"type\": \"Polygon\",");
        System.out.println("        \"coordinates\": [");
        System.out.println("          [");

        for (int i = 0; i < longitudes.length; i++) {
            System.out.println("            [");
            System.out.println("              " + longitudes[i] + ",");
            System.out.println("              " + latitudes[i]);
            System.out.println("            ],");
        }

        System.out.println("            [");
        System.out.println("              " + longitudes[0] + ",");
        System.out.println("              " + latitudes[0]);
        System.out.println("            ]");
        System.out.println("          ]");
        System.out.println("        ]");
        System.out.println("      }");
        System.out.println("    },");
        System.out.println("    {");
        System.out.println("      \"type\": \"Feature\",");
        System.out.println("      \"properties\": {},");
        System.out.println("      \"geometry\": {");
        System.out.println("        \"type\": \"Point\",");
        System.out.println("        \"coordinates\": [");
        System.out.println("          " + longitudes[0] + ",");
        System.out.println("          " + latitudes[0]);
        System.out.println("        ]");
        System.out.println("      }");
        System.out.println("    }");
        System.out.println("  ]");
        System.out.println("}");
    }

    /**
     * Prints list of coordinates as a way
     *
     * @param latitudes
     * @param longitudes
     */
    public static void DebugPrintWayByCoordinates(double[] latitudes, double[] longitudes) {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        for (int i = 0; i < longitudes.length; i++) {       // used before: FileReader.nodeIds.size()
            System.out.println(getGeoJsonElement(6, longitudes[i], latitudes[i]));

            if (i != longitudes.length - 1) {
                System.out.print("}, ");
            } else {
                System.out.println("}]");
                System.out.println("}");
            }
        }
    }

    /**
     * Helper method to get coordinates of the grid graph dots for initial debugging purpose
     */
    public static void DebugPrintGridGraph() {
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int row;
        int col;
        double latitude;
        double longitude;

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            row = GridGraph.idToRow(i);
            col = GridGraph.idToCol(i);
            if (!GridGraph.vertexData[i]) {
                longitude = GridGraph.colToLongitude(col);
                longitudes.add(longitude);
                latitude = GridGraph.rowToLatitude(row);
                latitudes.add(latitude);
            }
        }
        GeoJson.DebugprintNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());
    }

    /**
     * Helper Method to print a polygon for debug purposes
     *
     * @param longitudes
     * @param latitudes
     */
    public static void DebugPrintPolyline(double[] longitudes, double[] latitudes) {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        System.out.println("    {");
        System.out.println("      \"type\": \"Feature\",");
        System.out.println("      \"properties\": {},");
        System.out.println("      \"geometry\": {");
        System.out.println("        \"type\": \"LineString\",");
        System.out.println("        \"coordinates\": [");

        for (int i = 0; i < longitudes.length - 1; i++) {
            System.out.println("            [");
            System.out.println("              " + longitudes[i] + ",");
            System.out.println("              " + latitudes[i]);
            System.out.println("            ],");
        }

        System.out.println("            [");
        System.out.println("              " + longitudes[longitudes.length - 1] + ",");
        System.out.println("              " + latitudes[longitudes.length - 1]);
        System.out.println("            ]");
        System.out.println("        ]");
        System.out.println("      }");
        System.out.println("    },");
        System.out.println("    {");
        System.out.println("      \"type\": \"Feature\",");
        System.out.println("      \"properties\": {},");
        System.out.println("      \"geometry\": {");
        System.out.println("        \"type\": \"Point\",");
        System.out.println("        \"coordinates\": [");
        System.out.println("          " + longitudes[0] + ",");
        System.out.println("          " + latitudes[0]);
        System.out.println("        ]");
        System.out.println("      }");
        System.out.println("    },");
        System.out.println("    {");
        System.out.println("      \"type\": \"Feature\",");
        System.out.println("      \"properties\": {},");
        System.out.println("      \"geometry\": {");
        System.out.println("        \"type\": \"Point\",");
        System.out.println("        \"coordinates\": [");
        System.out.println("          " + longitudes[longitudes.length - 1] + ",");
        System.out.println("          " + latitudes[latitudes.length - 1]);
        System.out.println("        ]");
        System.out.println("      }");
        System.out.println("    }");
        System.out.println("  ]");
        System.out.println("}");
    }

    public static String generateRoute(double[] longitudes, double[] latitudes) {
        String route = "{\"type\":\"LineString\", \"coordinates\":[";
        for (int i = 0; i < longitudes.length; i++) {
            route += "[" + longitudes[i] + "," + latitudes[i] + "]";
            if (i < longitudes.length - 1) {
                route += ",";
            }
        }
        route = route + "]}";
        return route;
    }
}
