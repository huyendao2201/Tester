package Bai4;


public class PaymentCalculator {

    public enum Type { MALE, FEMALE, CHILD }

    public static int calculate(Type type, int age) {
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (age < 0 || age > 145) {
            throw new IllegalArgumentException("Age must be in range 0..145");
        }

        if (type == Type.CHILD) {
            if (age <= 17) return 50;
            throw new IllegalArgumentException("Child age must be 0..17");
        }

        // MALE / FEMALE
        if (age < 18) {
            throw new IllegalArgumentException("Male/Female age must be 18..145");
        }

        if (type == Type.MALE) {
            if (age <= 35) return 100;
            if (age <= 50) return 120;
            return 140; // 51..145
        } else { // FEMALE
            if (age <= 35) return 80;
            if (age <= 50) return 110;
            return 140; // 51..145
        }
    }
}
