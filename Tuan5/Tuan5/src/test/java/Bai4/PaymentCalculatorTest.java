package Bai4;

import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentCalculatorTest {

    // ----- CHILD -----
    @Test
    public void child_age0_payment50() {
        assertEquals(50, PaymentCalculator.calculate(PaymentCalculator.Type.CHILD, 0));
    }

    @Test
    public void child_age17_payment50() {
        assertEquals(50, PaymentCalculator.calculate(PaymentCalculator.Type.CHILD, 17));
    }

    @Test(expected = IllegalArgumentException.class)
    public void child_age18_invalid() {
        PaymentCalculator.calculate(PaymentCalculator.Type.CHILD, 18);
    }

    // ----- MALE -----
    @Test
    public void male_age18_100() {
        assertEquals(100, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 18));
    }

    @Test
    public void male_age35_100() {
        assertEquals(100, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 35));
    }

    @Test
    public void male_age36_120() {
        assertEquals(120, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 36));
    }

    @Test
    public void male_age50_120() {
        assertEquals(120, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 50));
    }

    @Test
    public void male_age51_140() {
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 51));
    }

    @Test
    public void male_age145_140() {
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 145));
    }

    // ----- FEMALE -----
    @Test
    public void female_age18_80() {
        assertEquals(80, PaymentCalculator.calculate(PaymentCalculator.Type.FEMALE, 18));
    }

    @Test
    public void female_age35_80() {
        assertEquals(80, PaymentCalculator.calculate(PaymentCalculator.Type.FEMALE, 35));
    }

    @Test
    public void female_age36_110() {
        assertEquals(110, PaymentCalculator.calculate(PaymentCalculator.Type.FEMALE, 36));
    }

    @Test
    public void female_age50_110() {
        assertEquals(110, PaymentCalculator.calculate(PaymentCalculator.Type.FEMALE, 50));
    }

    @Test
    public void female_age51_140() {
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.Type.FEMALE, 51));
    }

    // ----- VALIDATION -----
    @Test(expected = IllegalArgumentException.class)
    public void type_null_invalid() {
        PaymentCalculator.calculate(null, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void age_negative_invalid() {
        PaymentCalculator.calculate(PaymentCalculator.Type.MALE, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void age_146_invalid() {
        PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 146);
    }

    @Test(expected = IllegalArgumentException.class)
    public void male_age17_invalid() {
        PaymentCalculator.calculate(PaymentCalculator.Type.MALE, 17);
    }
}
