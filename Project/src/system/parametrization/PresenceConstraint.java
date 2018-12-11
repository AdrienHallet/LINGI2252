package system.parametrization;

import controllers.Controller;
import system.House;
import system.HousePart;
import system.parametrization.FeatureModelConstraint;

public class PresenceConstraint extends FeatureModelConstraint {
    Controller[] lhsControllers;
    Controller[] rhsControllers;

    public PresenceConstraint(Controller[] lhsControllers, Controller[] rhsControllers) {
        this.lhsControllers = lhsControllers;
        this.rhsControllers = rhsControllers;
    }

    @Override
    public void enforceConstraint(House house) throws BadConfigException {
        if (this.checkLhs(house)) {
            this.enforceRhs(house);
        }
    }

    private boolean checkLhs(House house) {
        if (this.lhsControllers == null)
            return true;
        for (Controller controller : lhsControllers) {
            boolean currentControllerPresent = false;
            for (HousePart housePart : house.housePartList) {
                if (housePart.hasController(controller)) {
                    currentControllerPresent = true;
                    break;
                }
            }
            if (!currentControllerPresent)
                return false;
        }
        return true;
    }

    private void enforceRhs(House house) throws BadConfigException {
        for (Controller controller : rhsControllers) {
            boolean currentControllerPresent = false;
            for (HousePart housePart : house.housePartList) {
                if (housePart.hasController(controller)) {
                    currentControllerPresent = true;
                    break;
                }
            }
            if (!currentControllerPresent)
                throw new BadConfigException(controller.type+" missing");
        }
    }
}
