package controllers;

public abstract class Controller {

    public int id = -1;
    public String type;
    public double value = 0.0;
    private boolean enabled = true;

    // DEPRECATED
    public void update(double newValue) {
        System.out.println("Using Controller.update() is DEPRECATED!");
        this.value = newValue;
    }

    /*
     * Sets the value to the default functioning (non-alert) value,
     * override if default value is not 0
     */
    public void reset() {
        this.value = 0.0;
    }

    /*
     * Returns a string that can be displayed to the user to show the state of
     * the actuator.
     */
    public abstract String getStateAsString();

    /*
     * Triggers the controller (for example, an alarm actuator would go off,
     * a motion sensor would detect motion, etc.)
     */
    public void trigger() {
        this.value = 100.0;
    }
    public boolean isTriggered() {
        return (this.value > 0.0);
    }

    public void enable() {
        this.enabled = true;
    }
    public void disable() {
        this.enabled = false;
    }
}
