package controllers;

public abstract class Controller {

    public int id = -1;
    public String type;
    public double value = 0.0;

    public void update(double newValue) {
        this.value = newValue;
    }

    public abstract String getStateAsString();
}
