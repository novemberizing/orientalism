package net.novemberizing.orientalism;

import java.util.Random;

public class OrientalismApplicationRandom {
    private static Random random;
    public static void gen() {
        synchronized (OrientalismApplicationRandom.class) {
            if(random == null) {
                random = new Random(System.currentTimeMillis());
            }
        }
    }

    public static int integer() {
        return random.nextInt();
    }

    public static int integer(int bound) {
        return random.nextInt(bound);
    }
}
