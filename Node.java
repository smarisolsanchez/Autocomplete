/**
 * @author Harry Smith
 */

public class Node {

    private int population;
    private String city;
    private String state;
    private String state_id;
    private double lat;
    private double lng;
    private int index;

    private double[] distances;

    /**
     * Initialize a Node with an empty string and 0 weight; useful for
     * writing tests.
     */
    public Node() {

    }

    /**
     * Initialize a Node with the given query string and weight.
     * @throws IllegalArgumentException if query is null or if weight is negative.
     */
    public Node(String city, String state, String state_id, double lat, double lng, int pop, int index) {
        if (pop < 0 || city == null || Math.abs(lat) > 85.05115 || Math.abs(lng) > 180) {
            throw new IllegalArgumentException("bad construction");
        }
        population = pop;
        this.city = city;
        this.state = state;
        this.state_id = state_id;
        this.lat = lat;
        this.lng = lng;
        this.index = index;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getPopulation() {
        return population;
    }
    public double[] getDistances() {
        return distances;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getIndex() {
        return index;
    }

    public void setReferences(double[] distances) {
        this.distances = distances;
    }
}
