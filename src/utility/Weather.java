package utility;

import java.util.Random;

public enum Weather {
    SUNNY,
    RAIN,
    THUNDERSTORM;

    public static Weather getRandom(Random random) {
        int roll = random.nextInt(100); // 0–99

        if (roll < 70) {       // 70% chance
            return SUNNY;
        } else if (roll < 95) { // 25% chance
            return RAIN;
        } else {               // 5% chance
            return THUNDERSTORM;
        }
    }
}
