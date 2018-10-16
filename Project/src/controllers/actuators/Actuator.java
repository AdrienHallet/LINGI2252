package controllers.actuators;

import controllers.Controller;

public abstract class Actuator extends Controller {

    public abstract String getStateAsString();
}
