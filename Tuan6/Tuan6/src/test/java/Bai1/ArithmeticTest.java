package Bai1;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ArithmeticTest {

    public String message = "JUnit testing exception demo";
    JUnitMessage junitMessage = new JUnitMessage(message);

    @Test(expected = ArithmeticException.class)
    public void testJUnitMessage() throws Exception {
        System.out.println("JUnit Exception Test is running...");
        junitMessage.printMessage();
    }

    @Test
    public void testJUnitHiMessage() {
        message = "Hello JUnit - " + message;
        System.out.println("JUnit Normal Test is running...");
        assertEquals(message, junitMessage.printHiMessage());
    }
}
