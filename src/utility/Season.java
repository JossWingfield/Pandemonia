package utility;

import utility.Season;

public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER;

    public Season next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
