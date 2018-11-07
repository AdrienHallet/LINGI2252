package controllers.connectedObjects;

import controllers.Controller;

public abstract class ConnectedObject extends Controller {

    public static final String RADIO = "radio";

    public void toggle(){
        if (isTriggered())
            reset();
        else
            trigger();
    }
}
