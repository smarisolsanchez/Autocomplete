import org.junit.Test;
import org.junit.Assert;

import java.awt.*;
import java.io.*;

public class NodeTest {
    @Test
    public void createNodeNoInput() {

        Node n = new Node();

        Assert.assertEquals(null,n.getTerm());


    }

    @Test
    public void createNodeWithInput() {

        Node n = new Node("hello",5);

        Assert.assertEquals(5, n.getTerm().getWeight());

    }

    @Test
    public void testGetTerm() {

        Node n = new Node("hello",5);

        Term t = new Term("hello",5);

        Assert.assertEquals(t.getWeight(),n.getTerm().getWeight());
        Assert.assertEquals(t.getTerm(),n.getTerm().getTerm());

    }

    @Test
    public void testSetTerm() {

        Node n = new Node("hello",5);
        Term t = new Term("bye",3);
        n.setTerm(t);

        Assert.assertEquals(3, n.getTerm().getWeight());
        Assert.assertEquals("bye", n.getTerm().getTerm());


    }

    @Test
    public void testSetAndGetWords() {

        Node n = new Node("hello", 5);
        n.setWords(1);

        Assert.assertEquals(1, n.getWords());

    }

    @Test
    public void testGetAndSetPrefixes() {

        Node n = new Node("hello", 5);
        n.setPrefixes(1);

        Assert.assertEquals(1, n.getPrefixes());

    }


    @Test
    public void testGetAndSetReferences() {

        Node n = new Node("hello", 5);
        Node[] ref = new Node[26];

        Node m = new Node("bye", 3);
        m.setWords(1);
        ref[0] = m;

        n.setReferences(ref);

        Assert.assertEquals(1,n.getReferences()[0].getWords());



    }


}
