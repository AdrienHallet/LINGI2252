package system.parametrization;

import org.json.JSONArray;
import org.json.JSONObject;
import system.House;
import system.HousePart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public abstract class Parametrization { // implements the design pattern "strategy"
    House house;

    public Parametrization(House house) throws BadConfigException {
        this.house = house;
    }

    /**
     * Instantiate a house from the config file
     */
    public void parseConfig(String configFilename) throws BadConfigException {
        try {
            JSONObject config = new JSONObject(readFile(configFilename));
            JSONArray houseParts = config.getJSONObject("house").getJSONArray("houseParts");
            parseHouseParts(houseParts);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        this.enforcePresenceConstraints();
    }

    /**
     * Parse the houseParts in the config file into usable objects
     * @param houseParts the house's houseParts
     */
    private void parseHouseParts(JSONArray houseParts){
        this.house.housePartList = new ArrayList<>();
        for (int i = 0; i < houseParts.length(); i++){
            try {
                JSONObject housePart = houseParts.getJSONObject(i);
                this.house.housePartList.add(new HousePart(this.house, housePart));
            } catch(Exception e) {
                System.err.println(e);
            }
        }
        this.house.linkHouseParts();
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

    abstract void enforcePresenceConstraints() throws BadConfigException;
    abstract void enforceActivationConstraints() throws BadConfigException;
}
