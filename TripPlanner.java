import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 *
 *
 */
public class TripPlanner implements ITravelPlanner {

    private GraphM graph;
    private int threshold;
    private int numCities;
    private HashMap<String, Integer> dictionary;
    private LinkedList<Node> nodes;



    //constructor
    public TripPlanner() {
        this.dictionary = new HashMap<>();
        this.nodes = new LinkedList<>();
    }

    public HashMap<String, Integer> getDictionary() {
        return dictionary;
    }

    public Node getCity(int id) {
        return nodes.get(id - 1);
    }

    public double distance(double lat1, double lat2, double lon1,
                           double lon2) {

        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return (Math.sqrt(distance) * 0.00062137);
    }

    public void doTheGraph (BufferedReader br) throws IOException {
        String line = "";
        String splitBy = ",";
        while (((line = br.readLine()) != null))   //returns a Boolean value
        {
            String[] nodeInfo = line.split(splitBy);
            String city = nodeInfo[0];
            String state_id = nodeInfo[1];
            String state = nodeInfo[2];
            double lat = Double.parseDouble(nodeInfo[3]);
            double lng = Double.parseDouble(nodeInfo[4]);
            int pop = Integer.parseInt(nodeInfo[5]);
            int index = nodes.size() + 1;
            Node node = new Node(city, state_id, state, lat, lng, pop, index);
            // add to dictionary and nodesList
            nodes.add(node);
            graph.setValue(index, node);
            dictionary.put(city + " " + state_id, index);
        }
    }

    /**
     * Create a graph representation of the dataset. The first line of the file
     * contains the number of nodes. Keep in mind that the vertex with id 0 is
     * not actually considered present in your final graph!
     *
     * @param filePath the path of the data
     * @param threshold     the maximum distance one can travel at once to be added to consideration set graph
     * @return the number of entries (nodes) in the dataset (graph)
     */
    @Override
    public int buildMap(String filePath, int threshold) {
        String line = "";
        String splitBy = ",";
        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String num = br.readLine();

            int i = 0;

            // Create an object of filereader
            // class with CSV file as a parameter.
            // create csvReader object passing
            // file reader as a parameter

            this.graph = new GraphM();

//            this.numCities = Integer.valueOf(num[0]);
            this.numCities = 498;
            graph.init(numCities + 1);

            doTheGraph(br);

            //add edges to graph
            //add edge if weight is at least tau

            for (Node c1 : nodes) {
                for (Node c2 : nodes) {
                    double distance = distance(c1.getLat(), c2.getLat(), c1.getLng(), c2.getLng());
                    if (distance < threshold) {
                        graph.addEdge(c1.getIndex(), c2.getIndex(), distance);
                        graph.addEdge(c2.getIndex(), c1.getIndex(), distance);
                    }


                }
            }

            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        this.threshold = threshold;
        return numCities;
    }

//    /**
//     * Create a graph representation of the dataset. The first line of the file
//     * contains the number of nodes. Keep in mind that the vertex with id 0 is
//     * not actually considered present in your final graph!
//     *
//     * @param filePath the path of the data
//     * @param threshold     the maximum distance one can travel at once to be added to consideration set graph
//     * @return the number of entries (nodes) in the dataset (graph)
//     */
//    @Override
//    public int buildMap(String filePath, int threshold) {
//        try {
//            Scanner scanner = new Scanner(new FileReader(filePath));
//            scanner.useDelimiter(",");   //sets the delimiter pattern
//            this.graph = new GraphM();
//            String input = scanner.nextLine();
////            this.numCities = Integer.parseInt(input);
//            this.numCities = 498;
//            graph.init(numCities + 1);
//
//            //add edges to graph
//            //add edge if weight is at least tau
//            for (int i = 0; i < numCities; i++) {
//                String city = scanner.next();
//                String state_id = scanner.next();
//                String state = scanner.next();
//                double lat = scanner.nextDouble();
//                double lng = scanner.nextDouble();
//                int pop = scanner.nextInt();
//                if (pop < threshold) {
//                    continue;
//                }
//                int index = nodes.size() + 1;
//                Node node = new Node(city, state_id, state, lat, lng, pop, index);
//                // add to dictionary and nodesList
//                nodes.add(node);
//                graph.setValue(index, node);
//                dictionary.put(city + " " + state_id, index);
//            }
//
//            for (Node c1 : nodes) {
//                for (Node c2 : nodes) {
//                    double distance = distance(c1.getLat(), c2.getLat(), c1.getLng(), c2.getLng());
//                    graph.addEdge(c1.getIndex(), c2.getIndex(), distance);
//                    graph.addEdge(c2.getIndex(), c1.getIndex(), distance);
//                }
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        this.threshold = threshold;
//        return numCities;
//    }

    /**
     * return the shortest path between two vertices
     * include the source and destination vertices in your collection
     * @param source      - the id of the origin node
     * @param destination - the id of the destination node
     * @return collection of nodes to follow to go from source to destination
     */
    public List<Integer> shortestPath(String source, String destination) {
        List<Integer> path = new LinkedList<>();
        double[] distances = new double[graph.nodeCount()];
        int[] parents = new int[graph.nodeCount()];
        dijkstra(dictionary.get(source), distances, parents);
        int curr = dictionary.get(destination);
        int sourceIndex = dictionary.get(source);
        path.add(curr);
        for (int i = 0; i < distances[dictionary.get(destination)]; i++) {
            if (curr != sourceIndex) {
                path.add(parents[curr]);
                curr = parents[curr];
            }
        }
        Collections.reverse(path);

        if (path.size() < 2) {
            return new LinkedList<>();
        }
        return path;
    }

    private int minVertex(double[] distances, int[] visited) {
        double min = INFINITY;
        int vertex = -1;
        for (int i = 1; i < graph.nodeCount(); i++) {
            if (visited[i] != VISITED) {
                vertex = i;
                break;
            }
        }
        min = distances[vertex];
        for (int i = 0; i < graph.nodeCount(); i++) {
            if (visited[i] != VISITED && distances[i] < min) {
                min = distances[i];
                vertex = i;
            }
        }
        return vertex;
    }

    private void doInfinity (double[] distances) {
        for (int i = 0; i < graph.nodeCount(); i++) {
            distances[i] = INFINITY;
        }
    }

    private void doNeighbors (double[] distances, int[] parents, int[] nList, int v) {
        for (int j = 0; j < nList.length; j++) {
            int w = nList[j];
            //100 - weight normalizes the numbers such that we can find shortest path
            if (distances[w] > (distances[v] + graph.distance(v,w))) {
                parents[w] = v;
                distances[w] = distances[v] + graph.distance(v,w);
            }
        }
    }

    private void dijkstra(int s, double[] distances, int[] parents) {
        int[] visited = new int[graph.nodeCount()];

        doInfinity(distances);

        distances[s] = 0;
        visited[0] = VISITED;
        parents[s] = -1;
        for (int i = 0; i < graph.nodeCount(); i++) {
            int v = minVertex(distances, visited);
            visited[v] = VISITED;
            if (distances[v] == INFINITY) {
                return;
            }
            int[] nList = graph.neighbors(v);
            doNeighbors(distances,parents,nList,v);
        }
    }

    public String printCities (List<Integer> sp) {

        String line = "";

        for (int i = 0; i < sp.size(); i++) {
            if (i == sp.size() - 1) {
                line += getCity(sp.get(i)).getCity() + " " + getCity(sp.get(i)).getState();
            }

            if (i != sp.size() - 1) {
                line += getCity(sp.get(i)).getCity() + " " + getCity(sp.get(i)).getState() + " -> ";
            }
        }
        return line;
    }

    /**
     * Return the distances of a specific vertex
     *
     * @param id the id of the vertex
     * @return the array of neighbor(s)
     */
    @Override
    public double[] getDistances(int id) {
        return graph.distances(id);
    }




}