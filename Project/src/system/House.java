package system;

import org.json.*;
import system.parametrization.BadConfigException;
import system.parametrization.ConcreteParametrization;
import system.parametrization.Parametrization;

import java.util.ArrayList;

public class House{

    private static House instance;
    private static String filename;

    JSONObject config;
    public ArrayList<HousePart> housePartList;
    Parametrization paramComponent;


    public static House getOrCreate(){
        return instance;
    }

    public static House getOrCreate(String configFilename) throws BadConfigException {
        if (instance != null && configFilename != null && configFilename.equals(filename)) {
            return instance;
        } else {
            instance = new House(configFilename);
            filename = configFilename;
            return instance;
        }
    }

    private House(String configFilename) throws BadConfigException {
        paramComponent = new ConcreteParametrization(this); // enforces presence constraints
        paramComponent.parseConfig(configFilename);
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


    public void linkHouseParts(){
        for(HousePart housePart : housePartList)
            housePart.linkSensors();
    }

    public ArrayList<HousePart> getRoomList() {
        return housePartList;
    }
}
