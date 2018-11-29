package system.parametrization;

import controllers.Controller;
import controllers.actuators.Actuator;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;
import system.House;
import system.HousePart;

public class ActivationConstraint extends FeatureModelConstraint {
    Controller shouldBeActivatedController;

    public ActivationConstraint(Controller shouldBeActivatedController) {
        this.shouldBeActivatedController = shouldBeActivatedController;
    }

    @Override
    public void enforceConstraint(House house) throws BadConfigException {
        if (shouldBeActivatedController instanceof Sensor) {
            for (HousePart housePart : house.housePartList) {
                for (Sensor sensor : housePart.sensors) {
                    if (!sensor.isTriggered())
                        throw new BadConfigException(shouldBeActivatedController.type+" is not activated");
                }
            }
        } else if (shouldBeActivatedController instanceof Actuator) {
            for (HousePart housePart : house.housePartList) {
                for (Actuator actuator : housePart.actuators) {
                    if (!actuator.isTriggered())
                        throw new BadConfigException(shouldBeActivatedController.type+" is not activated");
                }
            }
        } else if (shouldBeActivatedController instanceof ConnectedObject) {
            for (HousePart housePart : house.housePartList) {
                for (ConnectedObject connectedObject : housePart.connectedObjects) {
                    if (!connectedObject.isTriggered())
                        throw new BadConfigException(shouldBeActivatedController.type+" is not activated");
                }
            }
        }
    }
}
