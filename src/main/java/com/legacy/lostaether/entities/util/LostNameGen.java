package com.legacy.lostaether.entities.util;

import java.util.Random;

public class LostNameGen
{

	public static Random rand = new Random();

    public static String[] whaleNamePrefix = { "Raf", "Ro", "Ye", "Si", "Schw", "Spla" };

    public static String[] whaleNameMiddix = { "inkl", "ch", "flo", "", ""};

    public static String[] whaleNameSuffix = { "dorf", "umps", "dul", "dum", "er"};

    public static String whaleGen()
    {

        String result = "";

        result += whaleNamePrefix[rand.nextInt(whaleNamePrefix.length)];
        result += whaleNameMiddix[rand.nextInt(whaleNameMiddix.length)];
        result += whaleNameSuffix[rand.nextInt(whaleNameSuffix.length)];

        return result;
    }

}