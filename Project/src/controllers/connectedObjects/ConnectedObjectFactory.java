package controllers.connectedObjects;

import controllers.UnexistantControllerException;

public class ConnectedObjectFactory {

    public static ConnectedObject create(String type) throws UnexistantControllerException{
        switch (type){
            case ConnectedObject.RADIO:
                return new ConnectedRadio();
            default:
                throw new UnexistantControllerException(type);
        }
    }
}
