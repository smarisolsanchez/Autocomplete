import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScanFile {
    public static void main(String[] args) {
        String line = "";
        String splitBy = ",";
        try {
//parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("uscities_final.csv"));
            IAutocomplete ia = new Autocomplete();

            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] city = line.split(splitBy);    // use comma as separator
                //System.out.println("CityInfo [City Name=" + city[0] + ", State ID=" + city[1] + ", State Name=" + city[2] + ", lat=" + city[3] + ", lng= " + city[4] + ", Population= " + city[5] + "]");
                ia.addWord(city[0],Integer.getInteger(city[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

