
<img src="./Frontend/Logo.png" alt="drawing" width="200"> <br/>
Repository for the University Stuttgart "Fachpraktikum"
Join Meetings with this Link: https://jitsi-meet.fmi.uni-stuttgart.de/AlgLabCourseMeetingSS20

# Manual
- Import the project into [IntelliJ Community Edition](https://www.jetbrains.com/de-de/idea/download/#section=windows)
- Go to the VM-options of the main method and add
    - Xmx8192m as a parameter or higher to allow the project to allocate enough RAM
- Store the PBF-files in OSMMapData-folder
    - ***all files need to end on ".pbf"*** to be detected by the filereader in the backend 
- Start the backend
- Choose the wanted file in the dialog with its listed number and confirm with ***[Enter]***
- Go through the dialog as follows
    - the route will be displayed as a geojson LineString, which can be copy pasted into the http://geojson.io website

    
<br/>
    
# Analysis of the Optimization part

## Run 1 
source (1000runs,grid_size=500000,date=18.09;19.01.csv)
| Analysis      |              |                  |   |                 |              |                 |              |                  |               |
|---------------|--------------|------------------|---|-----------------|--------------|-----------------|--------------|------------------|---------------|
|               | Time speedup |  Nodes used less |   | t dijkstra avg. | t astar avg. |  total dijkstra |  total astar |  dijkstra routes |  astar routes |
| Average all   | 2.73616507   | 0.17531607       |   | 0.53736895      | 0.19639493   | 2557982         | 448455       | 1000             | 1000          |
|               |              |                  |   |                 |              |                 |              |                  |               |
| Average found | 2.82310778   | 0.16944788       |   | 0.54061034      | 0.19149476   | 2573363         | 436050       | 987              | 987           |
 
<br/>

### Best Case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| 5.22      | -21.42        | 41.22       | -21.42       | 0.5342578      | 0.0011721     | 2573363              | 101                  | true            | true           | 671    |

<img src="./BenchmarkData/BenchmarkPictures/500k_best_case.png" alt="drawing" width=80%>
 <br/>

<br/>

### Worst case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -59.58    | -153.9        | 75.78       | 42.66        | 0.5364174      | 1.2640244     | 2573363              | 2195931              | true            | true           | 780    |

<img src="./BenchmarkData/BenchmarkPictures/500k_worst_case.png" alt="drawing" width=80%>
 <br/>


<br/>

### Median of A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -5.22     | 173.7         | -40.86      | 78.66        | 0.5276572      | 0.1196866     | 2573363              | 295116               | true            | true           | 722    |

<img src="./BenchmarkData/BenchmarkPictures/500k_median_case.png" alt="drawing" width=80%>
 <br/>

---

<br/>

## Run 2
source (1000runs,grid_size=2000000,date=18.09;19.57.csv)

| Analysis      |              |                  |   |                 |              |                 |              |                  |               |
|---------------|--------------|------------------|---|-----------------|--------------|-----------------|--------------|------------------|---------------|
|               | Time speedup |  Nodes used less |   | t dijkstra avg. | t astar avg. |  total dijkstra |  total astar |  dijkstra routes |  astar routes |
| Average all   | 3.0265237    | 0.16416927       |   | 2.40409423      | 0.79434178   | 10400828        | 1707496      | 1000             | 1000          |
|               |              |                  |   |                 |              |                 |              |                  |               |
| Average found | 3.04224758   | 0.16333008       |   | 2.41134051      | 0.7926181    | 10432125        | 1703879      | 996              | 996           |

<br/>

### Best Case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| 23.31     | -62.37        | 32.49       | -62.73       | 2.4018132      | 0.0154737     | 10432125             | 54                   | true            | true           | 775    |

<img src="./BenchmarkData/BenchmarkPictures/2m_best_case.png" alt="drawing" width=80%>
 <br/>

<br/>

### Worst case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -61.65    | 146.25        | 25.29       | -96.75       | 2.3890516      | 3.9453193     | 10432125             | 8752452              | true            | true           | 935    |

<img src="./BenchmarkData/BenchmarkPictures/2m_worst_case.png" alt="drawing" width=80%>
 <br/>

<br/>

### Median of A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -51.93    | 101.97        | -62.91      | 1.71         | 2.4009995      | 0.4972607     | 10432125             | 1199228              | true            | true           | 9      |

<img src="./BenchmarkData/BenchmarkPictures/2m_median_case.png" alt="drawing" width=80%>
 <br/>

<br/>

## Run 3
(source 1000runs,grid_size=8000000,date=19.09;03.26.csv)

| Analysis      |              |                  |   |                 |              |                 |              |                  |               |
|---------------|--------------|------------------|---|-----------------|--------------|-----------------|--------------|------------------|---------------|
|               | Time speedup |  Nodes used less |   | t dijkstra avg. | t astar avg. |  total dijkstra |  total astar |  dijkstra routes |  astar routes |
| Average all   | 2.95581833   | 0.17024893       |   | 11.3515217      | 3.84039897   | 41854149        | 7125624      | 1000             | 1000          |
|               |              |                  |   |                 |              |                 |              |                  |               |
| Average found | 2.99941957   | 0.16775219       |   | 11.3511014      | 3.78443268   | 41854149        | 7021125      | 997              | 997           |

<br/>

### Best Case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -58.005   | 144.675       | -60.615     | 144.405      | 11.2043713     | 0.0152742     | 41854149             | 66                   | true            | true           | 849    |

<img src="./BenchmarkData/BenchmarkPictures/8m_best_case.png" alt="drawing" width=80%>
 <br/>

<br/>

### Worst case A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| -19.125   | 154.665       | 55.575      | 12.375       | 10.9503689     | 19.1131436    | 41854149             | 33517960             | true            | true           | 748    |

<img src="./BenchmarkData/BenchmarkPictures/8m_worst_case.png" alt="drawing" width=80%>
 <br/>

<br/>

### Median of A star (in terms of node pulls)

| start lat |  start   long |  dest   lat |  dest   long |  dijkstra time |  astar   time |  dijsktra node pulls |   astar   node pulls |  dijkstra found |  astar   found |  index |
|-----------|---------------|-------------|--------------|----------------|---------------|----------------------|----------------------|-----------------|----------------|--------|
| 87.615    | 49.455        | 54.675      | -179.505     | 11.6028939     | 1.9738589     | 41854149             | 5011196              | true            | true           | 593    |

<img src="./BenchmarkData/BenchmarkPictures/8m_median_case.png" alt="drawing" width=80%>
 <br/>

<br/>

---
#### Task 1: Understand OSM Data Structures
- As a first step, we need to get to know how the data is organized inside OSM.we are primarily interested in “nodes” and “ways”.
    - Resources
        - the [OSM wiki](https://wiki.openstreetmap.org/wiki/Main_page) explains the [core data structures](https://wiki.openstreetmap.org/wiki/Elements) as well as interpretations of individual tags.
        - this [youtube-playlist](https://www.youtube.com/playlist?list=PLCE6296A33CF47955) explains the OSM primitives and tags with lots of examples
#### Task 2: Extract Coastlines from a PBF File
- Read in a PBF file and extract all coastlines contained in the file. You can start by just outputting them to console.  To see your results, save them as GeoJson and use geojson.io. Merge touching coastline ways to enablethe further processing in the following tasks. It makes sense to store the coastlines in a format that is easy to read for future use.
    - Resources
        - Download of OSM data by region from [geofabrik](https://download.geofabrik.de/index.html) (Antarctica is a good starting point)
        - [PBF Format](https://wiki.openstreetmap.org/wiki/PBF_Format)
        - Definition of the [coastline tag](https://wiki.openstreetmap.org/wiki/Coastline)
        - [Geojson format definition](https://geojson.org/)
        - [Geojson.io](http://geojson.io) a GeoJson visualization site
        - [Geojson styling guide](https://github.com/mapbox/simplestyle-spec/tree/master/1.1.0): adding CSS properties that are supported by geojson.io
        - Detailed [tagging information](https://wiki.openstreetmap.org/wiki/Tag:natural%3Dcoastline) for coastlines
 
#### Task 3: Distinguish between Water and Land
Implement the point in polygon test to determine if a certain position is inthe ocean (aka passable for ships). Be aware the latitude and longitude arenot coordinates on a plane. Use the spherical model of the earth to do your calculations.
- Resources
    - [Many calculations for lat long points](http://www.movable-type.co.uk/scripts/latlong.html)
    - [Calculations with lat long but converted into vectors first](http://www.movable-type.co.uk/scripts/latlong-vectors.html)
    - [In-polygon test with spherical polygons](https://link.springer.com/article/10.1007/BF00894449) (Uni network only)
    - https://en.wikipedia.org/wiki/Point_in_polygon
    - [Video with 3D animation of spherical vs vector coordinates](https://www.youtube.com/watch?v=FDyenWWlPdU)            
            
#### Task 4: Grid Graph
- Implement a grid graph representation which allows routing on the oceanscorresponding to the input. Use a bit vector to distinguish between accessible nodes (in the ocean) and non-accessible nodes (on land). Node position and edges should not explicitly be stored but be calculated on demand.

#### Task 5: Dijkstra’s Algorithm
Implement Dijkstra’s Algorithm for shortest paths on your grid data structure.

#### Task 6: Interactive Routing Front End
Add a GUI to your project. It should be possible to set start and end nodesas well as visualizing the route. The calculated distance should be displayedin your GUI.

#### Task 7: Speed Things Up
Implement a speed-up technique/heuristic for your project.

#### Dependencies used (located in dependencies folder):
- Osmosis Pbf
    - Is used to parse PBF Files in java
    - Downloaded from https://mvnrepository.com/artifact/org.openstreetmap.osmosis/osmosis-pbf/0.46
    - Komplettes Osmosis Archiev: https://github.com/openstreetmap/osmosis/releases/tag/0.47.4
    - Tutorials to use: https://neis-one.org/2017/10/processing-osm-data-java/
- osm4j
    - used to parsePBF files in Java (alternative for Osmosis)
    - https://github.com/topobyte/osm4j-extra


        
#### Tutorial used
- ##### Task 3
    - [Tutorial for vector matheatics](http://www.movable-type.co.uk/scripts/latlong-vectors.html)
    - [Point in polygon test](http://geomalgorithms.com/a03-_inclusion.html)
    - Sorting Arrays with index List https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array-after-sorting
    - https://howtodoinjava.com/sort/collections-sort/
        

