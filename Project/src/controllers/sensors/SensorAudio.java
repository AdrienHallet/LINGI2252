package controllers.sensors;

public class SensorAudio extends Sensor{

    public SensorAudio(Boolean broadcast){
        super();
        this.type = AUDIO;
    }

    public String getStateAsString() {
        if (this.value > 0.0)
            return "I hear something";
        return "The sound of silence";
    }
}
