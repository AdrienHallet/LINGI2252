package controllers.connectedObjects;

public class ConnectedRadio extends ConnectedObject {

    public ConnectedRadio(){
        this.type = "radio";
    }

    public String getStateAsString(){
        if(this.value > 0.0)
            return "*music*";
        return "*silence*";
    }

    public void enable(){
        super.enable();
        this.value = 100.0;
    }

    public void disable(){
        super.disable();
        this.value = 0.0;
    }
}
