
import org.junit.Test;
import org.junit.Assert;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TermTest {

    @Test
    public void testCreateTerm() {
        Term t = new Term("hello",5);

        Assert.assertEquals(5, t.getWeight());
    }

    @Test
    public void testCompareTo() {
        Term a = new Term("apple",5);
        Term b = new Term("apply",5);

        Assert.assertEquals(1,b.compareTo(a));

    }

    @Test
    public void testToString() {
        Term a = new Term("apple",5);
        Assert.assertEquals("5" + "\t" + "apple",a.toString());
    }

    @Test
    public void testGetAndSetWeight() {
        Term t = new Term("hello",5);
        t.setWeight(5);
        Assert.assertEquals(5, t.getWeight());

    }

    @Test
    public void testGetAndSetTerm() {

        Term t = new Term("hello",5);
        t.setTerm("bye");

        Assert.assertEquals("bye",t.getTerm());

    }


    @Test
    public void testTheCompareReverse() {
        ITerm b = new Term("actor",2);
        ITerm c = new Term("acting",3);

        Assert.assertEquals(-1, b.getWeight() - c.getWeight());
    }

    @Test
    public void testByReverseOrder() {
        List<ITerm> words = new ArrayList<>();
        ITerm a = new Term("act",1);
        ITerm b = new Term("actor",2);
        ITerm c = new Term("acting",3);
        words.add(a);
        words.add(b);
        words.add(c);

        Collections.sort(words,ITerm.byReverseWeightOrder());

        List<ITerm> wordsT = new ArrayList<>();
        wordsT.add(c);
        wordsT.add(b);
        wordsT.add(a);

        Assert.assertEquals(wordsT, words);

    }

    @Test
    public void testByPrefixOrder() {
        List<ITerm> words = new ArrayList<>();
        ITerm a = new Term("c" +
                "arrot",1);
        ITerm b = new Term("act",1);
        ITerm c = new Term("born",1);
        words.add(a);
        words.add(b);
        words.add(c);

        Collections.sort(words,ITerm.byPrefixOrder(1));

        List<ITerm> wordsT = new ArrayList<>();
        wordsT.add(b);
        wordsT.add(c);
        wordsT.add(a);

        Assert.assertEquals(wordsT, words);



    }

    @Test
    public void testByOrder() {
        List<ITerm> words = new ArrayList<>();
        ITerm a = new Term("actor",1);
        ITerm b = new Term("act",1);
        ITerm c = new Term("acting",1);
        words.add(a);
        words.add(b);
        words.add(c);

        Collections.sort(words);

        List<ITerm> wordsT = new ArrayList<>();
        wordsT.add(b);
        wordsT.add(c);
        wordsT.add(a);

        Assert.assertEquals(wordsT, words);


    }
}
