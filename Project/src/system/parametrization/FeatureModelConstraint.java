package system.parametrization;

import system.House;

public abstract class FeatureModelConstraint {
    abstract void enforceConstraint(House house) throws BadConfigException;
}
