package Bai2;

import static org.junit.Assert.fail;
import org.junit.Test;

public class PersonTest3 {

    @Test
    public void testExpectedExceptionTryCatch() {
        try {
            new Person("Try Catch Test", -10);
            fail("Expected IllegalArgumentException but none was thrown");
        } catch (IllegalArgumentException e) {
            // Test pass vì đã bắt đúng ngoại lệ
        }
    }
}
