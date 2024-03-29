package controllers;

public abstract class Controller {

    public String type;
    public double value = 0.0;
    private boolean enabled = true;
    private boolean inverted = false;

    /*
     * Sets the value to the default functioning (non-alert) value,
     * override if default value is not 0
     */
    public void reset() {
        this.value = 0.0;
    }

    /**
     * Invert the sensor. So it returns 100-value.
     */
    public void setInverted(boolean inversion){
        this.inverted = inversion;
    }

    /**
     * Returns the inversion state
     * @return inverted
     */
    public boolean isInverted(){
        return this.inverted;
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
        if(enabled)
            return (this.value > 0.0);
        else
            return false;
    }

    public void enable() {
        this.enabled = true;
    }
    public void disable() {
        this.enabled = false;
    }
}
