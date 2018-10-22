package controllers.actuators;

public class ActuatorAudioAlarm extends Actuator {

    String sound = "BEEP BEEP BEEP BEEP";

    public ActuatorAudioAlarm() {
        this.type = AUDIO;
    }

    public String getStateAsString(){
        if (this.isTriggered())
            return sound;
        return "*silence*";
    }
}
