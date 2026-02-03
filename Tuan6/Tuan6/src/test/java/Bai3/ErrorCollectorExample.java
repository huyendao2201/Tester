package Bai3;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class ErrorCollectorExample {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void example() {

        collector.addError(new Throwable("Lỗi dữ liệu: tuổi không hợp lệ ở dòng 1"));
        collector.addError(new Throwable("Lỗi dữ liệu: email sai định dạng ở dòng 2"));

        System.out.println("Bắt đầu kiểm thử ErrorCollector...");

        try {
            Assert.assertTrue("A" == "B");
        } catch (Throwable t) {
            collector.addError(t);
        }

        System.out.println("Kết thúc kiểm thử - đã thu thập lỗi.");
    }
}
