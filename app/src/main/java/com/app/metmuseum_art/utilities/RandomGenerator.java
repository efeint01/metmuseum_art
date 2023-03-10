package com.app.metmuseum_art.utilities;

import java.util.Random;

public class RandomGenerator {
    /**
     * min and max are to be understood inclusively
     */
    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
}
