import controllers.actuators.*;
import controllers.connectedObjects.ConnectedObject;
import controllers.connectedObjects.ConnectedRadio;
import controllers.sensors.*;
import org.json.*;

public class HousePart {

    public String name;
    public String[] accessibleHouseParts;
    public Sensor[] sensors;
    public Actuator[] actuators;
    public ConnectedObject[] connectedObjects;

    /**
     * Create a room object from the config.json relevant part.
     * Note that a room is not *required* to be a real room.
     * You can decide that a room is split in two rooms to change the
     * system's granularity
     * @param room the room configuration
     */
    HousePart(JSONObject room){
        try{
            this.name = room.getString("name");
            this.accessibleHouseParts = stripArray(room.getJSONArray("accessible-houseParts").toString());
            if (room.has("sensors"))
                this.sensors = parseSensors(room.getJSONArray("sensors"));
            if (room.has("actuators"))
                this.actuators = parseActuators(room.getJSONArray("actuators"));
            if (room.has("objects"))
                this.connectedObjects = parseConnectedObjects(room.getJSONArray("objects"));
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

    /**
     * Print the state of the room, consisting of all the actuators' states
     */
    public void printState(){
        if (actuators != null)
            for (Actuator cActuator : actuators)
                System.out.format("[%s:%s]: %s\n", name, cActuator.type, cActuator.getStateAsString());
    }

    /**
     * Set the value of all actuators of given type
     * @param type the actuator type
     */
    public void setActuatorTypeToValue(String type, double value){
        if (actuators != null){
            for (Actuator cActuator : actuators){
                if (cActuator.type.equals(type)){
                    cActuator.value = value;
                }
            }
        }
    }
    /**
     * Helper method to format a JSONArray
     * @param array the JSONArray
     * @return the extracted elements
     */
    String[] stripArray(String array){
        /* Parse the rooms in the JSON into usable objects */
        String stripped = array.replaceAll("\\[","").replaceAll("]","");
        stripped = stripped.replaceAll("\"","");
        return stripped.split(",");
    }

    /**
     * Parse the sensors in a room and which actuators they are linked to
     * @param sList the sensors
     * @return the parsed list of sensors
     * Todo Extract Switch
     */
    Sensor[] parseSensors(JSONArray sList){
        /* Parse the sensors in the room into usable objects */
        Sensor[] list = new Sensor[sList.length()];
        for (int i = 0; i < sList.length(); i++){
            try {
                // Create a sensor
                JSONObject sensor = (JSONObject) sList.get(i);
                String type = sensor.getString("type");
                Boolean broadcast = false;
                if(sensor.has("broadcast")) {
                    broadcast = sensor.getBoolean("broadcast");
                }

                //Create the actuators and link them to the sensors
                JSONArray actions = sensor.getJSONArray("actions");
                Actuator[] aList = new Actuator[actions.length()];

                //Loop over each actuator and add it to the sensor's list
                for(int cAction = 0; cAction < actions.length(); cAction++){
                    JSONObject cActuator = (JSONObject) actions.get(cAction);
                    String cType = cActuator.getString("actuator");
                    Actuator newActuator = ActuatorFactory.create(cType);
                    newActuator.linkHousePartName = cActuator.getString("housePart");
                    aList[cAction] = newActuator;
                }

                list[i] = SensorFactory.create(type, broadcast);
                list[i].setActuatorList(aList);
            }catch (Exception ignored){}
        }
        return list;
    }

    /**
     * Parse the actuators in a room
     * @param aList the actuators
     * @return the parsed list of actuators
     * ToDo extract switch
     */
    Actuator[] parseActuators(JSONArray aList){
        /* Parse the actuators in the room into usable objects */
        Actuator[] list = new Actuator[aList.length()];
        for (int i = 0; i < aList.length(); i++){
            try {
                String type = aList.get(i).toString();
                list[i] = ActuatorFactory.create(type);
            }catch (Exception ignored){}
        }
        return list;
    }

    /**
     * Parse the connected objects in the room
     * @param coList the objects
     * @return the parsed list of objects
     */
    ConnectedObject[] parseConnectedObjects(JSONArray coList){
        ConnectedObject[] list = new ConnectedObject[coList.length()];
        for (int i = 0; i < coList.length(); i++){
            try{
                String type = coList.get(i).toString();
                ConnectedObject newConnectedObject;
                switch(type) {
                    case "radio":
                        newConnectedObject = new ConnectedRadio();
                        list[i] = newConnectedObject;
                        break;
                    default:
                        System.err.println("Error: Unsupported connected object type");
                }
            }catch (Exception ignored){}
        }
        return list;
    }
}
