import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


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
            } else {
                Collections.reverse(path);
                return path;
            }
        }
        Collections.reverse(path);

        if (path.size() < 2) {
            return new LinkedList<>();
        }
        return path;
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

        if (sp.size() < 2) {
            return "You don't have enough time to travel!";
        }

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



    public List<Integer> planRandom (String source, String destination, int threshold, int days, String filepath) {

        List<Integer> li = new ArrayList();

        buildMap(filepath, threshold*60);

        Queue<Node> q = new ArrayDeque<>();

        Set<Integer> vertices = new HashSet<>();
        vertices.add(dictionary.get(source));
        q.add(graph.getValue(dictionary.get(source)));

        int maxCost = Integer.MIN_VALUE;


        while (!q.isEmpty())
        {
            // dequeue front node
            Node node = q.poll();

            int v = node.getIndex();
            int cost = node.getPopulation();
             //set of node visited

            // if the destination is reached and BFS depth is equal to `m`,
            // update the minimum cost calculated so far
            if (cost > 0) {
                maxCost = Math.max(maxCost, cost);
            }

            li.add(v);

            if (v == getDictionary().get(destination)) {
                return li;
            }

            // do for every adjacent edge of `v`
            for (int edge: graph.neighbors(v))
            {
                // check for a cycle
                if (!vertices.contains(edge))
                {
                    // add current node to the path
                    vertices.add(edge);

                    // push every vertex (discovered or undiscovered) into
                    // the queue with a cost equal to the
                    // parent's cost plus the current edge's weight
                    q.add(graph.getValue(edge));
                }
            }
            int distance = 0;
            int prev = li.get(0);
            //calculate shortest distance
            for (int i = 1; i < li.size(); i++) {
                int curr = li.get(i);
                distance += graph.distance(prev, curr);
                prev = curr;
            }

            if (distance > (threshold*60*days - threshold*60)) {
                if (li.size() < 2) {
                    System.out.print("You don't have enough time to travel!");
                    return new ArrayList<>();
                }
                return li;
            }

        }




        return li;

    }


    //TODO: hello

    private void popDijkstra(int s, double[] distances, int[] parents) {
        int[] visited = new int[graph.nodeCount()];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = -INFINITY;
        }
        distances[s] = 0;
        visited[0] = VISITED;
        parents[s] = -1;
        for (int i = 0; i < graph.nodeCount(); i++) {
            int v = maxVertex(distances, visited);
            visited[v] = VISITED;
            if (distances[v] == -INFINITY) {
                return;
            }
            int[] nList = graph.neighbors(v);
            for (int j = 0; j < nList.length; j++) {
                int w = nList[j];
                //roughly find the most popular path
                if (distances[w] < (distances[v] + graph.getValue(w).getPopulation() / graph.distance(v, w))) {
                    parents[w] = v;
                    distances[w] = (distances[v] + graph.getValue(w).getPopulation() / graph.distance(v, w));
                }
            }
        }
    }

    /**
     * @param source
     * @param destination
     * @param hours
     * @param tripDays
     * @return list of stops on the trip which is within the time constraint
     */


    public List<Integer> planTrip(String source, String destination, int hours, int tripDays) {
        //assumption: average 60 mph
        int threshold = hours * 60;
        int totalDist = threshold * tripDays;
        buildMap("uscities_final.csv", threshold);
        //initialize output
        List<Integer> output = new LinkedList<>();
        //check random
        if (destination.equals("random")) {
//            return planRandom(source, threshold * tripDays, 0, 0, output);
            return output; //TEMPORARY
        } else {
            //is it possible
            List<Integer> shortest = shortestPath(source, destination);
            int shortestDistance = 0;
            int prev = shortest.get(0);
            //calculate shortest distance
            for (int i = 1; i < shortest.size(); i++) {
                int curr = shortest.get(i);
                shortestDistance += graph.distance(prev, curr);
                prev = curr;
            }
            if (totalDist < shortestDistance) {
                return new LinkedList<>();
            }

            List<Integer> plan = new LinkedList<>();
            double[] popularities = new double[graph.nodeCount()];
            int[] parents = new int[graph.nodeCount()];
            popDijkstra(dictionary.get(source), popularities, parents);

            int curr = dictionary.get(destination);
            int sourceIndex = dictionary.get(source);
            plan.add(curr);
            for (int i = 0; i < popularities[dictionary.get(destination)]; i++) {
                if (curr != sourceIndex) {
                    plan.add(parents[curr]);
                    curr = parents[curr];
                }
            }
            Collections.reverse(plan);

            return plan;
        }
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

    private int maxVertex(double[] distances, int[] visited) {
        double max = -INFINITY;
        int vertex = -1;
        for (int i = 1; i < graph.nodeCount(); i++) {
            if (visited[i] != VISITED) {
                vertex = i;
                break;
            }
        }
        max = distances[vertex];
        for (int i = 0; i < graph.nodeCount(); i++) {
            if (visited[i] != VISITED && distances[i] > max) {
                max = distances[i];
                vertex = i;
            }
        }
        return vertex;
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