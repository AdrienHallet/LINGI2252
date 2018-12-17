package system.parametrization;

import controllers.Controller;
import controllers.actuators.ActuatorAudioAlarm;
import controllers.actuators.ActuatorLock;
import controllers.actuators.ActuatorMotorDoor;
import controllers.sensors.SensorClock;
import controllers.sensors.SensorConsumption;
import controllers.sensors.SensorSmokeDetector;
import system.House;

import java.util.ArrayList;

public class ConcreteParametrization extends Parametrization {
    private LinkConstraint linkConstraint;
    private ArrayList<PresenceConstraint> presenceConstraints = new ArrayList<>();
    private ArrayList<ActivationConstraint> activationConstraints = new ArrayList<>();

    public ConcreteParametrization(House house) throws BadConfigException {
        super(house);
        initializeConstraints();
    }

    private void initializeConstraints() {
        // Hard-coded on basis of our feature model
        // Link constraints
        this.linkConstraint = new LinkConstraint();

        // Presence constraints
        Controller[] lhs = {new SensorConsumption(false)};
        Controller[] rhs = {new SensorClock(false)};
        presenceConstraints.add(new PresenceConstraint(lhs, rhs));

        Controller[] smokeDetector = {new SensorSmokeDetector(false)};
        presenceConstraints.add(new PresenceConstraint(null, smokeDetector));

        Controller[] alarm = {new ActuatorAudioAlarm()};
        presenceConstraints.add(new PresenceConstraint(null, alarm));

        // Activation constraints
//        Controller alwaysActive = new SensorSmokeDetector(false);
//        activationConstraints.add(new ActivationConstraint(alwaysActive));

        // When a motor of a door is opening the door, the locks should always be open
        Controller motorDoor = new ActuatorMotorDoor();
        ArrayList<Controller> ifTriggeredControllers = new ArrayList<>();
        ifTriggeredControllers.add(motorDoor);
        Controller lock = new ActuatorLock();
        activationConstraints.add(new ConditionedActivationConstraint(
           ifTriggeredControllers, false,
           lock, null
        ));
    }

    @Override
    public void enforcePresenceConstraints() throws BadConfigException {
        for (PresenceConstraint c : presenceConstraints) {
            c.enforceConstraint(this.house);
        }
    }

    @Override
    public void enforceActivationConstraints() throws BadConfigException {
        for (ActivationConstraint c : activationConstraints) {
            c.enforceConstraint(this.house);
        }
    }

    @Override
    public void enforceLinkConstraints() throws BadConfigException {
        linkConstraint.enforceConstraint(this.house);
    }
}
