import org.json.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class House{

    JSONObject config;
    ArrayList<HousePart> housePartList;


    /**
     * Instantiate a house from the config file
     */
    public House(String configFilename) {
        try {
            config = new JSONObject(readFile(configFilename));
            JSONArray houseParts = config.getJSONObject("house").getJSONArray("houseParts");
            parseHouseParts(houseParts);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Returns the housePart with given name, null if not found
     * @param housePartName the housePart name to search for
     * @return the housePart or null if not found
     */
    public HousePart getHousePartByName(String housePartName){
        for (HousePart cHousePart : housePartList)
            if (cHousePart.name.equalsIgnoreCase(housePartName))
                return cHousePart;
        return null;
    }


    /**
     * Parse the houseParts in the config file into usable objects
     * @param houseParts the house's houseParts
     */
    void parseHouseParts(JSONArray houseParts){
        housePartList = new ArrayList<>();
        for (int i = 0; i < houseParts.length(); i++){
            try {
                JSONObject housePart = houseParts.getJSONObject(i);
                housePartList.add(new HousePart(housePart));
            } catch(Exception ignored) {}
        }
    }

    /**
     * Simple helper method to read from file.
     * @param filename the file to read from
     * @return the file as a String (no need for lines, it's JSON)
     */
    private String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<HousePart> getRoomList() {
        return housePartList;
    }
}
