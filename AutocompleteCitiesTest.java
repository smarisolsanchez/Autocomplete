import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AutocompleteCitiesTest {
    @Test
    public void testGetTerm() {

        IAutocomplete ia = new Autocomplete();
        ia.addWord("New York", 18972871);

        String word = "New York";
        NodeA n = new NodeA();

        NodeA curr = n;

        for (char c : word.toCharArray()) {

            if (Math.abs(c - 'a') < 26 && Character.isLetter(c)) {
                NodeA[] children = curr.getReferences();
                if (children[Math.abs((c - 'a'))] == null) {
                    NodeA m = new NodeA();
                    m.setPrefixes(1);
                    children[Math.abs((c - 'a'))] = m;
                } else {
                    int numP = children[Math.abs((c - 'a'))].getPrefixes();
                    children[Math.abs((c - 'a'))].setPrefixes(numP + 1);

                }

                curr = children[Math.abs((c - 'a'))];


            }
        }

        curr.setTerm(new Term("new york",18972871));
        curr.setWords(1);

        Assert.assertEquals(curr.getTerm().term, ia.getSubTrie("new york").getTerm().term);
    }

    @Test
    public void testGetSuggestionsIndirectCities() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("san diego",3084174); //low pop
        ia.addWord("san francisco",3290197);
        ia.addWord("seattle",3438221); //high pop




        List toCompare = ia.getSuggestions("s");

        List newList = new ArrayList();
        newList.add(new Term("seattle",3438221)); //high pop
        newList.add(new Term("san francisco",3290197));
        newList.add(new Term("san diego",3084174)); // low pop




        Assert.assertEquals(newList.get(0).toString(),toCompare.get(0).toString());
        Assert.assertEquals(newList.get(1).toString(),toCompare.get(1).toString());
        Assert.assertEquals(newList.get(2).toString(),toCompare.get(2).toString());

    }

    @Test
    public void testGeneralAdding() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("uscities_final.csv",50);

        List toCompare = ia.getSuggestions("s");

        Assert.assertEquals("3438221\tseattle",toCompare.get(0).toString());

    }




}
