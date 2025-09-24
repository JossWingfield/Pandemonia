package utility;

import java.util.Random;

public enum Weather {
    SUNNY,
    RAIN,
    THUNDERSTORM;

    public static Weather getRandom(Random random) {
        int roll = random.nextInt(100); // 0–99

        if (roll < 50) {       // 50% chance
            return SUNNY;
        } else if (roll < 85) { // 35% chance
            return RAIN;
        } else {               // 15% chance
            return THUNDERSTORM;
        }
    }
}
