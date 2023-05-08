

public class Term implements ITerm {


    String term;

    long weight;




    /**
     * Initialize a Term with a given query String and weight
     */
    public Term(String term, long weight) {

        if (term == null || weight < 0) {
            throw new IllegalArgumentException();
        }

        this.term = term;
        this.weight = weight;
    }

    @Override
    public int compareTo(ITerm that) {
        int num = 0;
        if (this.term.compareTo(that.getTerm()) > 0) {
            num = 1;
        }
        if (this.term.compareTo(that.getTerm()) < 0) {
            num = -1;
        }
        return num;
    }

    @Override
    public String toString() {
        String tabs = "" + '\t';
        return (this.weight + tabs + this.term);
    }

    @Override
    public long getWeight() {
        return this.weight;
    }

    @Override
    public String getTerm() {
        return this.term;
    }

    @Override
    public void setWeight(long weight) {
        this.weight = weight;
    }

    @Override
    public String setTerm(String term) {
        return this.term = term;
    }


}
