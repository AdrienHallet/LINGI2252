package controllers.actuators;

public class ActuatorAudioAlarm extends Actuator {

    String sound = "BEEP BEEP BEEP BEEP"; // Quality code here

    public ActuatorAudioAlarm(){
        this.type = "audio-alarm";
    }

    public String getStateAsString(){
        if (this.value > 0.0)
            return sound;
        else
            return "*silence*";
    }

}
