package Bai2;


import org.junit.Test;

public class PersonTest2 {

    @Test(expected = IllegalArgumentException.class)
    public void testExpectedExceptionAnnotation() {
        new Person("Test Annotation", 0);
    }
}
