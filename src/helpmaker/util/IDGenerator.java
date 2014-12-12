package helpmaker.util;

import java.util.Random;

public class IDGenerator {
    private final static int MAX_VALUE = 8191;

    public static String generateID() {
        Random r = new Random();
        int part1 = (int)(Math.random()*MAX_VALUE);
        int part2 = (int)(Math.random()*MAX_VALUE);
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(part2));
        result.append(Integer.toString(part1));
        while (result.length() < 8) {
            result.insert(0, Integer.toHexString((int)(Math.random()*16)));
        }
        return result.toString().toUpperCase();
    }
}
