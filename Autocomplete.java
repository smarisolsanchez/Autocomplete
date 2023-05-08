import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Autocomplete implements IAutocomplete {

    Node root;

    int k;

    public Autocomplete() {
        root = new Node("",1);
        int k = 0;
    }

    public boolean isWord(String w)  {
        for (char c : w.toCharArray()) {
            if ((!(Character.isLetter(c))) && (!(Character.isWhitespace(c)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addWord(String word, long weight) {
        Node curr = this.root;



        word = word.toLowerCase();

        if (isWord(word)) {

            int now = curr.getPrefixes();
            curr.setPrefixes(now + 1);

            for (char c : word.toCharArray()) {
                if (Math.abs(c - 'a') < 26 && Character.isLetter(c)) {
                    Node[] children = curr.getReferences();
                    if (children[Math.abs((c - 'a'))] == null) {
                        Node n = new Node();
                        n.setPrefixes(1);

                        children[Math.abs((c - 'a'))] = n;
                    } else {
                        int numP = children[Math.abs((c - 'a'))].getPrefixes();
                        children[Math.abs((c - 'a'))].setPrefixes(numP + 1);

                    }
                    curr = children[Math.abs((c - 'a'))];
                }
            }

            curr.setTerm(new Term(word, weight));
            curr.setWords(1);

        }

    }

    /*
    @Override
    public Node buildTrie(String filename, int k) {

        if (k > 0) {
            this.k = k;
        }

        FileReader fr = null;

        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader r = new BufferedReader(fr);

        int total1;

        String lineRead = null;
        try {
            String total = r.readLine();
            total1 = Integer.parseInt(total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int count = 0;

        while ((count < total1)) {
            try {
                if (!r.ready()) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                lineRead = r.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String[] sArray = lineRead.split("\\s+");

            addWord(sArray[2], Long.parseLong(sArray[1]));

            count += 1;

        }

        return this.root;
    }

     */


    @Override
    public Node buildTrie(String filename, int k) {

        String line = "";
        String splitBy = ",";
        try {
//parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(filename));

            br.readLine();

            int i = 0;

            while (((line = br.readLine()) != null) && i < k)   //returns a Boolean value
            {
                String[] city = line.split(splitBy);    // use comma as separator
                //System.out.println("CityInfo [City Name=" + city[0] + ", State ID=" + city[1] + ", State Name=" + city[2] + ", lat=" + city[3] + ", lng= " + city[4] + ", Population= " + city[5] + "]");
                String s = city[0];
                Integer in = Integer.parseInt(city[5]);
                addWord(s,in);
                i += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.root;

    }

    @Override
    public Node getSubTrie(String prefix) {

        Node curr = this.root;

        for (char c : prefix.toCharArray()) {
            if (Math.abs(c - 'a') < 26) {
                if ((curr.getReferences()[Math.abs(c - 'a')]) == null) {
                    return null;
                } else {
                    curr = curr.getReferences()[Math.abs(c - 'a')];
                }
            }
        }


        return curr;
    }

    @Override
    public int countPrefixes(String prefix) {

        Node curr = this.root;

        if (isWord(prefix)) {

            for (char c : prefix.toCharArray()) {
                if ((curr.getReferences()[Math.abs(c - 'a')]) == null) {
                    return 0;
                } else {
                    curr = curr.getReferences()[Math.abs(c - 'a')];
                }
            }

        } else {
            return 0;
        }

        return curr.getPrefixes();
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
            } else {
                if ((n != null)) {
                    suggestionHelper(n,list);
                }
            }

        }

    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {
        List<ITerm> toReturn = new ArrayList<>();
        //return copies
        Node curr = this.root;



        if (isWord(prefix)) {
            for (char c : prefix.toCharArray()) {
                if ((curr.getReferences()[Math.abs(c - 'a')]) == null) {
                    return toReturn;
                } else {
                    curr = curr.getReferences()[Math.abs(c - 'a')];
                }
            }
            suggestionHelper(curr, toReturn);
        }
        if (!(toReturn.isEmpty())) {
            Collections.sort(toReturn, Collections.reverseOrder());
        }

        List<ITerm> last = new ArrayList<>();

        for (int i = 0; i < toReturn.size(); i++) {
            last.add(toReturn.get(i));
        }
        return last;
    }
}
