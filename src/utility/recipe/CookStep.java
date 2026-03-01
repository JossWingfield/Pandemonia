package utility.recipe;

public class CookStep {

    private String station;
    private boolean optional;

    public CookStep(String station) {
        this(station, false);
    }

    public CookStep(String station, boolean optional) {
        this.station = station;
        this.optional = optional;
    }

    public boolean matches(CookStep other) {
        if (optional) return true;

        return station.equals(other.station);
    }

    public String getStation() { return station; }
    public boolean isOptional() { return optional; }
}
