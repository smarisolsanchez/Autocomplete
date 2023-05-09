import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.*;

public class NodeTest {
    @Test
    public void createNodeNoInput() {

        NodeA n = new NodeA();

        Assert.assertEquals(null,n.getTerm());


    }

    @Test
    public void createNodeWithInput() {

        NodeA n = new NodeA("hello",5);

        Assert.assertEquals(5, n.getTerm().getWeight());

    }

    @Test
    public void testGetTerm() {

        NodeA n = new NodeA("hello",5);

        Term t = new Term("hello",5);

        Assert.assertEquals(t.getWeight(),n.getTerm().getWeight());
        Assert.assertEquals(t.getTerm(),n.getTerm().getTerm());

    }

    @Test
    public void testSetTerm() {

        NodeA n = new NodeA("hello",5);
        Term t = new Term("bye",3);
        n.setTerm(t);

        Assert.assertEquals(3, n.getTerm().getWeight());
        Assert.assertEquals("bye", n.getTerm().getTerm());


    }

    @Test
    public void testSetAndGetWords() {

        NodeA n = new NodeA("hello", 5);
        n.setWords(1);

        Assert.assertEquals(1, n.getWords());

    }

    @Test
    public void testGetAndSetPrefixes() {

        NodeA n = new NodeA("hello", 5);
        n.setPrefixes(1);

        Assert.assertEquals(1, n.getPrefixes());

    }


    @Test
    public void testGetAndSetReferences() {

        NodeA n = new NodeA("hello", 5);
        NodeA[] ref = new NodeA[26];

        NodeA m = new NodeA("bye", 3);
        m.setWords(1);
        ref[0] = m;

        n.setReferences(ref);

        Assert.assertEquals(1,n.getReferences()[0].getWords());



    }


}
