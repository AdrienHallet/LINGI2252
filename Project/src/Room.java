import controllers.actuators.Actuator;
import controllers.actuators.ActuatorAudioAlarm;
import controllers.sensors.Sensor;
import controllers.sensors.SensorLight;
import controllers.sensors.SensorMotion;
import controllers.sensors.SensorSmokeDetector;
import org.json.*;

import java.util.ArrayList;

public class Room {

    public String name;
    public String[] accessibleRooms;
    public Sensor[] sensors;
    public Actuator[] actuators;

    Room(JSONObject room){
        try{
            this.name = room.getString("name");
            this.accessibleRooms = stripArray(room.getJSONArray("accessible-rooms").toString());
            if (room.has("sensors"))
                this.sensors = parseSensors(room.getJSONArray("sensors"));
            if (room.has("actuators"))
                this.actuators = parseActuators(room.getJSONArray("actuators"));
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void startFire(){
        /* As the fire starts, trigger a smoke detector (if any, too bad if not) */
        for (Sensor cSensor : sensors){
            if (cSensor.type.equals("smoke"))
                cSensor.trigger();
        }
    }

    String[] stripArray(String array){
        /* Parse the rooms in the JSON into usable objects */
        String stripped = array.replaceAll("\\[","").replaceAll("]","");
        stripped = stripped.replaceAll("\"","");
        return stripped.split(",");
    }

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
                    String cType = actions.get(cAction).toString();
                    Actuator cActuator;
                    switch(cType) {
                        case "audio-alarm":
                            cActuator = new ActuatorAudioAlarm();
                            break;
                        default:
                            cActuator = null; //ToDo custom exception
                            System.err.println("Error: unsupported actuator type");
                            break;
                    }
                    aList[cAction] = cActuator;
                }

                Sensor newSensor = null;
                switch(type) {
                    case "motion":
                        newSensor = new SensorMotion(broadcast);
                        list[i] = newSensor;
                        break;
                    case "smoke":
                        newSensor = new SensorSmokeDetector(broadcast);
                        list[i] = newSensor;
                        break;
                    case "light":
                        newSensor = new SensorLight(broadcast);
                        list[i] = newSensor;
                        break;
                    default:
                        System.err.println("Wrong sensor type");
                        break;
                }
                assert newSensor != null;
                newSensor.setActuatorList(aList);
            }catch (Exception ignored){}
        }
        return list;
    }

    Actuator[] parseActuators(JSONArray aList){
        /* Parse the actuators in the room into usable objects */
        Actuator[] list = new Actuator[aList.length()];
        for (int i = 0; i < aList.length(); i++){
            try {
                String type = aList.get(i).toString();
                Actuator newActuator;
                switch(type) {
                    case "audio-alarm":
                        newActuator = new ActuatorAudioAlarm();
                        list[i] = newActuator;
                        break;
                    default:
                        System.err.println("Error: Unsupported actuator type"); //ToDo custom exception
                        break;
                }
            }catch (Exception ignored){}
        }
        return list;
    }
}
