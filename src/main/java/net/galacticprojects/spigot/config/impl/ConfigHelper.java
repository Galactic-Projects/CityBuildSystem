package net.galacticprojects.spigot.config.impl;

import java.util.Arrays;

public final class ConfigHelper {

    private ConfigHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String[] getKeys(final String input) {
        if (!input.contains(".")) {
            return new String[] {
                input
            };
        }
        return input.split("\\.");
    }

    public static String[] getNextKeys(final String[] input) {
        if (input.length == 1) {
            return new String[0];
        }
        final String[] output = new String[input.length - 1];
        for (int x = 1; x < input.length; x++) {
            output[x - 1] = input[x];
        }
        return output;
    }

    public static String getLastKey(final String[] input) {
        if (input.length != 0) {
            if (input.length == 1) {
                return input[0];
            }
            return input[input.length - 1];
        }
        return "";
    }

    public static String[] getKeysWithout(final String[] input, final String denied) {
        return Arrays.stream(input).filter(current -> !current.equals(denied)).toArray(String[]::new);
    }

}
