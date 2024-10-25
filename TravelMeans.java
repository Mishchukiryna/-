public class TravelMeans {
    private String name;
    private int lowestSpeed;
    private int highestSpeed;

    public TravelMeans(String name, int lowestSpeed, int highestSpeed) {
        this.name = name;
        this.lowestSpeed = lowestSpeed;
        this.highestSpeed = highestSpeed;
    }

    public String getName() {
        return name;
    }

    public int getMinSpeed() {
        return lowestSpeed;
    }

    public int getMaxSpeed() {
        return highestSpeed;
    }

    @Override
    public String toString() {
        return name + " (Speed range: " + lowestSpeed + " - " + highestSpeed + " km/h)";
    }
}

