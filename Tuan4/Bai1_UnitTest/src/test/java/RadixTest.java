import org.Radix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RadixTest {

    @Test
    void constructor_numberAm_throwIncorrectValue() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Radix(-1)
        );
        assertEquals("Incorrect Value", ex.getMessage());
    }

    @Test
    void constructor_numberBang0_throwIncorrectValue() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Radix(0)
        );
        assertEquals("Incorrect Value", ex.getMessage());
    }

    @Test
    void convert_radixNgoaiKhoang_throwInvalidRadix() {
        Radix r = new Radix(10);

        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> r.convertDecimalToAnother(1)
        );
        assertEquals("Invalid Radix", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> r.convertDecimalToAnother(17)
        );
        assertEquals("Invalid Radix", ex2.getMessage());
    }

    @Test
    void convert_macDinhLaHe2() {
        assertEquals("1010", new Radix(10).convertDecimalToAnother());
    }

    @Test
    void convert_he2() {
        assertEquals("1", new Radix(1).convertDecimalToAnother(2));
        assertEquals("10", new Radix(2).convertDecimalToAnother(2));
        assertEquals("1010", new Radix(10).convertDecimalToAnother(2));
        assertEquals("11111111", new Radix(255).convertDecimalToAnother(2));
    }

    @Test
    void convert_he8() {
        assertEquals("7", new Radix(7).convertDecimalToAnother(8));
        assertEquals("10", new Radix(8).convertDecimalToAnother(8));
        assertEquals("17", new Radix(15).convertDecimalToAnother(8));
        assertEquals("377", new Radix(255).convertDecimalToAnother(8));
    }

    @Test
    void convert_he16_coChuAdenF() {
        assertEquals("A", new Radix(10).convertDecimalToAnother(16));
        assertEquals("F", new Radix(15).convertDecimalToAnother(16));
        assertEquals("10", new Radix(16).convertDecimalToAnother(16));
        assertEquals("1F", new Radix(31).convertDecimalToAnother(16));
        assertEquals("FF", new Radix(255).convertDecimalToAnother(16));
        assertEquals("100", new Radix(256).convertDecimalToAnother(16));
    }

    @Test
    void convert_kiemTraCheoVoiMath_pow() {
        int[] numbers = {1, 2, 3, 10, 31, 64, 255, 1024, 9999};
        int[] radices = {2, 3, 5, 8, 10, 16};

        for (int num : numbers) {
            for (int radix : radices) {
                Radix r = new Radix(num);

                String expected = Integer.toString(num, radix).toUpperCase();
                String actual = r.convertDecimalToAnother(radix);

                assertEquals(expected, actual, "num=" + num + ", radix=" + radix);
            }
        }
    }
}
