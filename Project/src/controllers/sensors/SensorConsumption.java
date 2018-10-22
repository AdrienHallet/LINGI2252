package controllers.sensors;

public class SensorConsumption extends Sensor {
    double consumption = 0.0;

    public SensorConsumption(Boolean broadcast){
        super(broadcast);
        this.type = CONSUMPTION;
    }

    /**
     * Add a consumed amount to the total
     * @param amount the amount of (water/electricity/gaz) consumed
     * @return the new total (after addition)
     */
    public double consume(double amount){
        consumption += amount;
        return consumption;
    }

    public String getStateAsString(){
        return String.format("Current consumption : %%1$,.2f", consumption);
    }
}
