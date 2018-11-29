package system.parametrization;

import controllers.Controller;
import controllers.sensors.SensorClock;
import controllers.sensors.SensorConsumption;
import controllers.sensors.SensorSmokeDetector;
import system.House;

import java.util.ArrayList;

public class ConcreteParametrization extends Parametrization {
    private ArrayList<PresenceConstraint> presenceConstraints = new ArrayList<>();
    private ArrayList<ActivationConstraint> activationConstraints = new ArrayList<>();

    public ConcreteParametrization(House house) throws BadConfigException {
        super(house);
        initializeConstraints();
    }

    private void initializeConstraints() {
        // Hard-coded on basis of our feature model
        Controller[] lhs = {new SensorConsumption(false)};
        Controller[] rhs = {new SensorClock(false)};
        presenceConstraints.add(new PresenceConstraint(lhs, rhs));

        Controller alwaysActive = new SensorSmokeDetector(false);
        activationConstraints.add(new ActivationConstraint(alwaysActive));
    }

    @Override
    public void enforcePresenceConstraints() throws BadConfigException {
        System.out.println(this.presenceConstraints);
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
}
