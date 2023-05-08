import org.junit.Test;
import org.junit.Assert;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class AutocompleteTest {
    @Test
    public void testNewAutocomplete() {



    }

    @Test
    public void testAddWord() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("act", 1);

        String word = "act";
        Node n = new Node();

        Node curr = n;

        for (char c : word.toCharArray()) {

            if (Math.abs(c - 'a') < 26 && Character.isLetter(c)) {
                Node[] children = curr.getReferences();
                if (children[Math.abs((c - 'a'))] == null) {
                    Node m = new Node();
                    m.setPrefixes(1);
                    children[Math.abs((c - 'a'))] = m;
                } else {
                    int numP = children[Math.abs((c - 'a'))].getPrefixes();
                    children[Math.abs((c - 'a'))].setPrefixes(numP + 1);

                }

                curr = children[Math.abs((c - 'a'))];


            }
        }

        curr.setTerm(new Term("act",1));
        curr.setWords(1);




        Assert.assertEquals(curr.getTerm(), curr.getTerm());


    }

    @Test
    public void testAddWordInvalid() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("~", 3);

        Node n = new Node();



    }

    @Test
    public void testAddWordInvalidMoreThanOne() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("mar!sol", 3);
        ia.addWord("mariana", 3);
        ia.addWord("m4rian", 3);

        Assert.assertEquals(1, ia.getSubTrie("").getPrefixes());

    }


    @Test
    public void testBuildTriePokemon1Node() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt", 1);

        Assert.assertEquals(1, ia.getSubTrie("sci").getPrefixes());

    }

    @Test
    public void testBuildTriePokemonMultipleNodes() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt", 10001);

        Assert.assertEquals("zygarde", ia.getSubTrie("zygarde").getTerm().getTerm());

    }


    @Test
    public void testGetSubTrie() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);


        Assert.assertEquals(3,ia.getSubTrie("act").getPrefixes());


    }

    @Test
    public void testGetSubTrieNotThere() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);

        Node theSub = ia.getSubTrie("for");


        Assert.assertEquals(null,theSub);


    }

    @Test
    public void testGetSubTrieNotThereBuiltTree() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",10);

        Node theSub = ia.getSubTrie("mar!sol");


        Assert.assertEquals(null,theSub);


    }

    @Test
    public void testCountPrefixOfTrie() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",729);

        Node theSub = ia.getSubTrie("");


        Assert.assertEquals(729,theSub.getPrefixes());


    }

    @Test
    public void testCountPrefixes() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);

        int theNumber = ia.countPrefixes("act");
        Assert.assertEquals(3,theNumber);
    }

    @Test
    public void testCountPrefixesThatZero() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);

        int theNumber = ia.countPrefixes("for");
        Assert.assertEquals(0,theNumber);
    }

    public void suggestionHelper(Node curr, List<ITerm> list) {

        for (int i = 0; i < 26; i++) {
            Node n = curr.getReferences()[i];
            if ((n != null) && (n.getWords() != 0)) {
                if (n.getTerm() != null) {
                    Term t = n.getTerm();
                    list.add(new Term(t.getTerm(),t.getWeight()));
                    if (n.getPrefixes() > 1) {
                        suggestionHelper(n,list);
                    }
                }
                if (n.getPrefixes() > 1) {
                    suggestionHelper(n,list);
                }
            } else {
                if ((n != null)) {
                    suggestionHelper(n,list);
                }
            }

        }

    }

    @Test
    public void testSuggestionHelperDirectWord() {

        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);

        List theN = new ArrayList();

        Node n = ia.getSubTrie("ac");

        suggestionHelper(n,theN);


        List theL = new ArrayList<>();
        theL.add(new Term("act",1));
        theL.add(new Term("acting",1));
        theL.add(new Term("actor",1));

        Assert.assertEquals(theN.get(1).toString(),theL.get(1).toString());



    }

    @Test
    public void testSuggestionHelperIndirectWord() {

        IAutocomplete ia = new Autocomplete();
        ia.addWord("act",1);
        ia.addWord("acting",1);
        ia.addWord("actor",1);

        List theN = new ArrayList();

        Node n = ia.getSubTrie("a");

        suggestionHelper(n,theN);


        List theL = new ArrayList<>();
        theL.add(new Term("act",1));
        theL.add(new Term("acting",1));
        theL.add(new Term("actor",1));

        Assert.assertEquals(theL.get(0).toString(),theN.get(0).toString());



    }





    @Test
    public void testGetSuggestionsIndirectPokemon() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",1);


        List toCompare = ia.getSuggestions("s");

        List newList = new ArrayList();
        newList.add(new Term("sableye",232622));


        Assert.assertEquals(newList.get(0).toString(),toCompare.get(0).toString());


    }

    @Test
    public void testGetSuggestionsLengthPokemon() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",8);


        List toCompare = ia.getSuggestions("s");


        Assert.assertEquals(8,toCompare.size());


    }


    @Test
    public void testGetSuggestionsEmpty() {
        IAutocomplete ia = new Autocomplete();
        ia.addWord("mar!sol", 3);
        ia.addWord("mariana", 3);
        ia.addWord("m4rian", 3);

        List toCompare = ia.getSuggestions("a");

        Assert.assertTrue(toCompare.isEmpty());

    }



    @Test
    public void testGetSuggestionsCountPokemon() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",728);


        List toCompare = ia.getSuggestions("");


        Assert.assertEquals(728,toCompare.size());

    }


    @Test
    public void testCountPrefixesPokemon() {
        IAutocomplete ia = new Autocomplete();
        ia.buildTrie("pokemon.txt",3);


        Node theSub = ia.getSubTrie("");

        Assert.assertEquals(729,theSub.getPrefixes());

    }





}
