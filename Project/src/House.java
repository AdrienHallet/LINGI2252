import org.json.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

class House{

    JSONObject config;
    ArrayList<Room> roomList;


    /**
     * Instantiate a house from the config file
     */
    public House() {
        try {
            config = new JSONObject(readFile("src/config.json"));
            JSONArray rooms = config.getJSONObject("house").getJSONArray("rooms");
            parseRooms(rooms);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    /**
     * Parse the rooms in the config file into usable objects
     * @param rooms the house's rooms
     */
    void parseRooms(JSONArray rooms){
        roomList = new ArrayList<>();
        for (int i = 0; i < rooms.length(); i++){
            try {
                JSONObject room = rooms.getJSONObject(i);
                roomList.add(new Room(room));
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
}
