package controllers.connectedObjects;

import controllers.Controller;

public abstract class ConnectedObject extends Controller {

    public void toggle(){
        if (isTriggered())
            reset();
        else
            trigger();
    }
}
