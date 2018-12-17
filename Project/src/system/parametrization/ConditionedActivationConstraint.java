package system.parametrization;

import controllers.Controller;
import controllers.actuators.Actuator;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;
import system.House;
import system.HousePart;

import java.util.ArrayList;

public class ConditionedActivationConstraint extends ActivationConstraint {
    ArrayList<Controller> ifTriggeredControllers;
    boolean inverted = false;
    Controller mustBeNotTriggeredController;

    public ConditionedActivationConstraint(ArrayList<Controller> ifTriggeredControllers, boolean invertLHSController, Controller mustBeTriggeredController, Controller mustBeNotTriggeredController) {
        super(mustBeTriggeredController);
        this.ifTriggeredControllers = ifTriggeredControllers;
        this.inverted = invertLHSController;
        this.mustBeNotTriggeredController = mustBeNotTriggeredController;
    }

    @Override
    public void enforceConstraint(House house) throws BadConfigException {
        if (mustBeTriggeredController != null) {
            if (mustBeTriggeredController instanceof Sensor) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (Sensor sensor : housePart.sensors) {
                            if (!sensor.isTriggered())
                                throw new BadConfigException(mustBeTriggeredController.type + " is not activated");
                        }
                    }
                }
            } else if (mustBeTriggeredController instanceof Actuator) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (Actuator actuator : housePart.actuators) {
                            if (!actuator.isTriggered())
                                throw new BadConfigException(mustBeTriggeredController.type + " is not activated");
                        }
                    }
                }
            } else if (mustBeTriggeredController instanceof ConnectedObject) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (ConnectedObject connectedObject : housePart.connectedObjects) {
                            if (!connectedObject.isTriggered())
                                throw new BadConfigException(mustBeTriggeredController.type + " is not activated");
                        }
                    }
                }
            }
        }
        else if (mustBeNotTriggeredController != null) {
            if (mustBeNotTriggeredController instanceof Sensor) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (Sensor sensor : housePart.sensors) {
                            if (sensor.isTriggered())
                                throw new BadConfigException(mustBeNotTriggeredController.type + " is not activated");
                        }
                    }
                }
            } else if (mustBeNotTriggeredController instanceof Actuator) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (Actuator actuator : housePart.actuators) {
                            if (actuator.isTriggered())
                                throw new BadConfigException(mustBeNotTriggeredController.type + " is not activated");
                        }
                    }
                }
            } else if (mustBeNotTriggeredController instanceof ConnectedObject) {
                for (HousePart housePart : house.housePartList) {
                    if (this.checkCondition(housePart)) {
                        for (ConnectedObject connectedObject : housePart.connectedObjects) {
                            if (connectedObject.isTriggered())
                                throw new BadConfigException(mustBeNotTriggeredController.type + " is not activated");
                        }
                    }
                }
            }
        }
    }

    protected boolean checkCondition(HousePart housePart) {
        if (this.ifTriggeredControllers == null || this.ifTriggeredControllers.size() <= 0)
            return false;
        for (Controller c : this.ifTriggeredControllers) {
            if (c instanceof Sensor) {
                for (Sensor sensor : housePart.sensors) {
                    if (sensor.type.equals(c.type)) {
                        if (sensor.isTriggered() != this.inverted) // if it is triggered (with condition not inverted) or not triggered (with condition inverted)
                            return true;
                    }
                }
            }
            else if (c instanceof Actuator) {
                for (Actuator actuator : housePart.actuators) {
                    if (actuator.type.equals(c.type)) {
                        if (actuator.isTriggered() != this.inverted) // id.
                            return true;
                    }
                }
            }
            else if (c instanceof ConnectedObject) {
                for (ConnectedObject connectedObject : housePart.connectedObjects) {
                    if (connectedObject.type.equals(c.type)) {
                        if (connectedObject.isTriggered() != this.inverted) // id.
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
