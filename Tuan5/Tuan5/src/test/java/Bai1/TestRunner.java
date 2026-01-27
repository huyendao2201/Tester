package Bai1;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MathFuncTest.class);

        System.out.println("Run tests: " + result.getRunCount());
        System.out.println("Failed tests: " + result.getFailureCount());
        System.out.println("Ignored tests: " + result.getIgnoreCount());

        for (Failure failure : result.getFailures()) {
            System.out.println("Failure: " + failure.toString());
        }

        System.out.println("Success: " + result.wasSuccessful());
    }
}
