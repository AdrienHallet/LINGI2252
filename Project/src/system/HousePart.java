package system;

import controllers.Link;
import controllers.actuators.*;
import controllers.connectedObjects.ConnectedObject;
import controllers.connectedObjects.ConnectedRadio;
import controllers.sensors.*;
import org.json.*;

import java.security.InvalidParameterException;

public class HousePart {

    public House parentHouse;
    private JSONArray sensorsJSON;
    public String name;
    public String[] accessibleHouseParts;
    public Sensor[] sensors;
    public Actuator[] actuators;
    public ConnectedObject[] connectedObjects;

    /**
     * Create a housePart object from the config.json relevant part.
     * Note that a housePart is not *required* to be a real housePart.
     * You can decide that a housePart is split in two houseParts to change the
     * system's granularity
     * @param housePart the housePart configuration
     */
    HousePart(House parent, JSONObject housePart){
        parentHouse = parent;
        try{
            if (housePart.has("name") && !housePart.getString("name").equalsIgnoreCase(""))
                this.name = housePart.getString("name");
            else
                throw new InvalidParameterException("Invalid HousePart name");
            if (housePart.has("accessible-houseParts"))
                this.accessibleHouseParts = stripArray(housePart.getJSONArray("accessible-houseParts").toString());
            else
                this.accessibleHouseParts = new String[0];
            if (housePart.has("actuators"))
                this.actuators = parseActuators(housePart.getJSONArray("actuators"));
            else
                this.actuators = new Actuator[0];
            if (housePart.has("objects"))
                this.connectedObjects = parseConnectedObjects(housePart.getJSONArray("objects"));
            else
                this.connectedObjects = new ConnectedObject[0];
            if (housePart.has("sensors"))
                this.sensorsJSON = housePart.getJSONArray("sensors");
            else
                this.sensors = new Sensor[0];
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

    void linkSensors(){
        if(sensors == null)
            this.sensors = parseSensors(this.sensorsJSON);
    }

    /**
     * Print the state of the housePart, consisting of all the actuators' states
     */
    void printState(){
        if (actuators != null)
            for (Actuator cActuator : actuators)
                System.out.format("[%s:%s]: %s\n", name, cActuator.type, cActuator.getStateAsString());
    }

    /**
     * Set the value of the actuator of given type
     * @param type the actuator type
     */
    public void setActuatorTypeToValue(String type, double value){
        if (actuators != null){
            for (Actuator cActuator : actuators){
                if (cActuator.type.equals(type)){
                    cActuator.value = value;
                    return;
                }
            }
        }
        System.out.println("This actuator does not exist.");
    }

    /**
     * Set the value of the sensor of given type
     * @param type the actuator type
     */
    public void setSensorTypeToValue(String type, double value){
        if (sensors != null){
            for (Sensor cSensor : sensors){
                if (cSensor.type.equals(type)){
                    cSensor.value = value;
                    return;
                }
            }
        }
        System.out.println("This sensor does not exist");

    }

    Actuator getActuatorType(String type){
        for (Actuator actuator : actuators){
            if (actuator.type.equalsIgnoreCase(type)) {
                return actuator;
            }
        }
        return null;
    }
    /**
     * Helper method to format a JSONArray
     * @param array the JSONArray
     * @return the extracted elements
     */
    String[] stripArray(String array){
        /* Parse the houseParts in the JSON into usable objects */
        String stripped = array.replaceAll("\\[","").replaceAll("]","");
        stripped = stripped.replaceAll("\"","");
        String[] result = stripped.split(",");
        if (result.length == 1 && result[0].equals(""))
            return new String[0];
        return result;
    }

    /**
     * Parse the sensors in a housePart and which actuators they are linked to
     * @param sList the sensors
     * @return the parsed list of sensors
     */
    Sensor[] parseSensors(JSONArray sList){
        /* Parse the sensors in the housePart into usable objects */
        Sensor[] list = new Sensor[sList.length()];
        for (int i = 0; i < sList.length(); i++){
            try {
                // Create a sensor
                JSONObject sensor = (JSONObject) sList.get(i);
                String type = sensor.getString("type");
                boolean broadcast = false;
                if(sensor.has("broadcast")) {
                    broadcast = sensor.getBoolean("broadcast");
                }

                //Create the actuators and link them to the sensors
                JSONArray actions = sensor.getJSONArray("actions");
                Actuator[] aList = new Actuator[actions.length()];
                boolean inverted = false;

                for(int cAction = 0; cAction < actions.length(); cAction++) {
                    JSONObject cActuator = (JSONObject) actions.get(cAction);
                    String cType = cActuator.getString("actuator");
                    String housePart = cActuator.getString("housePart");
                    aList[cAction] = parentHouse.getHousePartByName(housePart).getActuatorType(cType);
                    if (cActuator.has("inverted")){
                        inverted = true;
                    }
                }

                list[i] = SensorFactory.create(type, broadcast);
                list[i].invert();
                list[i].setActuatorList(aList);
            }catch (Exception e){
                // This may be a problem for incorrectly encoded configurations (displaying it may be a good idea)
                System.err.println("Exception ignored in 'parseSensors': " + e.getMessage());
            }
        }
        return list;
    }

    /**
     * Parse the actuators in a housePart
     * @param aList the actuators
     * @return the parsed list of actuators
     */
    Actuator[] parseActuators(JSONArray aList){
        /* Parse the actuators in the housePart into usable objects */
        Actuator[] list = new Actuator[aList.length()];
        for (int i = 0; i < aList.length(); i++){
            try {
                String type = aList.get(i).toString();
                list[i] = ActuatorFactory.create(type);
            }catch (Exception e){
                // This may be a problem for incorrectly encoded configurations (displaying it may be a good idea)
                System.err.println("Exception ignored in 'parseActuators': " + e.getMessage());
            }
        }
        return list;
    }

    /**
     * Parse the connected objects in the housePart
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
            }catch (Exception e){
                // This may be a problem for incorrectly encoded configurations (displaying it may be a good idea)
                System.err.println("Exception ignored in 'parseConnectedObjects': " + e.getMessage());
            }
        }
        return list;
    }
}
