
/**
 * @author OpenDSA
 *
 */
public class GraphM implements Graph
{
    private double[][]  matrix;
    private Node[] nodeValues;
    private int      numEdge;


    /**
     * Empty no argument constructor
     */
    GraphM() {

    }



    // Initialize the graph with n vertices
    public void init(int n) {
        matrix = new double[n][n];
        nodeValues = new Node[n];
        numEdge = 0;
    }


    // Return the number of vertices
    public int nodeCount()
    {
        return nodeValues.length;
    }


    // Return the current number of edges
    public int edgeCount()
    {
        return numEdge;
    }


    // Get the value of node with index v
    public Node getValue(int v) {
        return nodeValues[v];
    }


    // Set the value of node with index v
    public void setValue(int v, Node val)
    {
        nodeValues[v] = val;
    }


    // Adds a new edge from node v to node w
    // Returns the new edge
    public void addEdge(int v, int w, double distance) {
        if (distance == 0)
            return; // Can't store weight of 0
        if (matrix[v][w] == 0)
            numEdge++;
        matrix[v][w] = distance;
    }


    // Get the weight value for an edge
    public double distance(int v, int w) {
        return matrix[v][w];
    }


    // Removes the edge from the graph.
    public void removeEdge(int v, int w)
    {
        if (matrix[v][w] != 0) {
            matrix[v][w] = 0;
            numEdge--;
        }
    }


    // Returns true iff the graph has the edge
    public boolean hasEdge(int v, int w)
    {
        return matrix[v][w] != 0;
    }


    // Returns an array containing the indicies of the neighbors of v
    public double[] distances(int v) {
        int i;
        int count = 0;
        double[] temp;

        for (i = 0; i < nodeValues.length; i++)
            if (matrix[v][i] != 0)
                count++;
        temp = new double[count];
        for (i = 0, count = 0; i < nodeValues.length; i++)
            if (matrix[v][i] != 0)
                temp[count++] = matrix[v][i];
        return temp;
    }

    // Returns an array containing the indicies of the neighbors of v
    public int[] neighbors(int v) {
        int i;
        int count = 0;
        int[] temp;

        for (i = 0; i < nodeValues.length; i++)
            if (matrix[v][i] != 0)
                count++;
        temp = new int[count];
        for (i = 0, count = 0; i < nodeValues.length; i++)
            if (matrix[v][i] != 0)
                temp[count++] = i;
        return temp;
    }
}
