package Bai2;


import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PersonTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testExpectedExceptionRule() {
        exception.expect(IllegalArgumentException.class);
        new Person("JUnit User", -5);
    }
}
