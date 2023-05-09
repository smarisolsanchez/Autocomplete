import java.util.List;

/**
 * Methods: buildMap, planTrip,
 */
public interface ITravelPlanner {
    /**
     *
     */
    public static final int VISITED  = -5;
    /**
     *
     */
    public static final double INFINITY = Double.MAX_VALUE;

    /**
     *
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     * @return distance in meters
     */
    public double distance(double lat1, double lat2, double lon1,
                           double lon2);

    /**
     * Create a graph representation of the dataset. The first line of the file
     * contains the number of nodes. Keep in mind that the vertex with id 0 is
     * not actually considered present in your final graph!
     *
     * @param filePath the path of the data
     * @param threshold the minimum edge weight required for an edge to be added to the graph.
     *           Will be a value between 0 and 1. Save this as an instance variable!
     * @return the number of entries (nodes) in the dataset (graph)
     */
    int buildMap(String filePath, int threshold);

    /**
     * Return the distances of a specific vertex
     *
     * @param id the id of the vertex
     * @return the array of neighbor(s)
     */
    double[] getDistances(int id);

    /**
     * return the shortest path between two vertices
     * include the source and destination vertices in your collection
     * @param source      - the id of the origin node
     * @param destination - the id of the destination node
     * @return collection of nodes to follow to go from source to destination
     */
    List<Integer> shortestPath(String source, String destination);


}