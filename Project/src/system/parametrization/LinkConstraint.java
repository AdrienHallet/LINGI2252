package system.parametrization;

import controllers.sensors.Sensor;
import system.House;
import system.HousePart;

public class LinkConstraint extends FeatureModelConstraint {
    /*
     * Checks that all the sensors are linked to an actuator
     */

    @Override
    public void enforceConstraint(House house) throws BadConfigException {
        for (HousePart housePart : house.housePartList) {
            for (Sensor sensor : housePart.sensors) {
                if (sensor.getActuatorList().length <= 0)
                    throw new BadConfigException("Sensor '"+sensor.type+"' does not trigger any actuator");
            }
        }
    }
}
