/**
 * @author Harry Smith
 */

public class NodeA {
    private Term term;
    private int words;
    private int prefixes;
    private NodeA[] references;

    /**
     * Initialize a Node with an empty string and 0 weight; useful for
     * writing tests.
     */
    public NodeA() {
        this.term = null;
        this.words = 0;
        this.prefixes = 0;
        this.references = new NodeA[66];
    }
    /**
     * Initialize a Node with the given query string and weight.
     * @throws IllegalArgumentException if query is null or if weight is negative.
     */
    public NodeA(String query, long weight) {

        if ((query == null) || (weight < 0)) {

            throw new IllegalArgumentException();
        }

        this.term = new Term(query,weight);

        this.words = 0;

        this.prefixes = 0;

        this.references = new NodeA[66];


    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    public NodeA[] getReferences() {
        return references;
    }

    public void setReferences(NodeA[] references) {
        this.references = references;
    }
}
