package controllers.connectedObjects;

public class ConnectedObjectNull extends ConnectedObject {
    public ConnectedObjectNull(){
        this.type = "null";
    }

    public String getStateAsString(){
        return "I am not a correct object";
    }
}
