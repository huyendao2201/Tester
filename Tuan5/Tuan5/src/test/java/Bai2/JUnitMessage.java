package Bai2;

public class JUnitMessage {

    private String message;

    public JUnitMessage(String message) {
        this.message = message;
    }

    public void printMessage() {
        int a = 10 / 0;
        System.out.println(message);
    }

    public String printHiMessage() {
        return "Hi!" + message;
    }
}
