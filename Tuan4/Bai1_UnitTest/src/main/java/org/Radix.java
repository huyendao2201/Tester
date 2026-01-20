package org;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Radix {
    private final int number;

    public Radix(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Incorrect Value");
        }
        this.number = number;
    }

    public String convertDecimalToAnother(int radix) {
        if (radix < 2 || radix > 16) {
            throw new IllegalArgumentException("Invalid Radix");
        }

        int n = this.number;
        List<String> result = new ArrayList<>();

        while (n > 0) {
            int value = n % radix;

            if (value < 10) {
                result.add(Integer.toString(value));
            } else {
                switch (value) {
                    case 10 -> result.add("A");
                    case 11 -> result.add("B");
                    case 12 -> result.add("C");
                    case 13 -> result.add("D");
                    case 14 -> result.add("E");
                    case 15 -> result.add("F");
                    default -> throw new IllegalStateException("Unexpected digit: " + value);
                }
            }

            n /= radix;
        }

        Collections.reverse(result);
        return String.join("", result);
    }

    public String convertDecimalToAnother() {
        return convertDecimalToAnother(2);
    }
}
