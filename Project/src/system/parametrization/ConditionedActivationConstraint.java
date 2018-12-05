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
    ArrayList<Controller> ifNotTriggeredControllers;
    Controller mustBeNotTriggeredController;

    public ConditionedActivationConstraint(ArrayList<Controller> ifTriggeredControllers, ArrayList<Controller> ifNotTriggeredControllers, Controller mustBeTriggeredController, Controller mustBeNotTriggeredController) {
        super(mustBeTriggeredController);
        this.ifTriggeredControllers = ifTriggeredControllers;
        this.ifNotTriggeredControllers = ifNotTriggeredControllers;
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
        return (this.checkPosCondition(housePart) && this.checkNegCondition(housePart));
    }
    protected boolean checkPosCondition(HousePart housePart) {
        if (this.ifTriggeredControllers == null || this.ifTriggeredControllers.size() <= 0)
            return true;
        for (Controller c : this.ifTriggeredControllers) {
            if (c instanceof Sensor) {
                for (Sensor sensor : housePart.sensors) {
                    if (sensor.type.equals(c.type)) {
                        if (!sensor.isTriggered())
                            return false;
                    }
                }
            }
            else if (c instanceof Actuator) {
                for (Actuator actuator : housePart.actuators) {
                    if (actuator.type.equals(c.type)) {
                        if (!actuator.isTriggered())
                            return false;
                    }
                }
            }
            else if (c instanceof ConnectedObject) {
                for (ConnectedObject connectedObject : housePart.connectedObjects) {
                    if (connectedObject.type.equals(c.type)) {
                        if (!connectedObject.isTriggered())
                            return false;
                    }
                }
            }
        }
        return true;
    }
    protected boolean checkNegCondition(HousePart housePart) {
        if (this.ifNotTriggeredControllers == null || this.ifNotTriggeredControllers.size() <= 0)
            return true;
        for (Controller c : this.ifNotTriggeredControllers) {
            if (c instanceof Sensor) {
                for (Sensor sensor : housePart.sensors) {
                    if (sensor.type.equals(c.type)) {
                        if (sensor.isTriggered())
                            return false;
                    }
                }
            }
            else if (c instanceof Actuator) {
                for (Actuator actuator : housePart.actuators) {
                    if (actuator.type.equals(c.type)) {
                        if (actuator.isTriggered())
                            return false;
                    }
                }
            }
            else if (c instanceof ConnectedObject) {
                for (ConnectedObject connectedObject : housePart.connectedObjects) {
                    if (connectedObject.type.equals(c.type)) {
                        if (connectedObject.isTriggered())
                            return false;
                    }
                }
            }
        }
        return true;
    }
}
