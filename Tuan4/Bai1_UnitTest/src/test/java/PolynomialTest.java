import org.Polynomial;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    void constructor_nAm_thiNemLoiInvalidData() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(-1, List.of(1, 2))
        );
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void constructor_thieuHoacThuaHeSo_thiNemLoiInvalidData() {
        // n = 2 => cần 3 hệ số
        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(2, List.of(1, 2)) // thiếu
        );
        assertEquals("Invalid Data", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(2, List.of(1, 2, 3, 4)) // thừa
        );
        assertEquals("Invalid Data", ex2.getMessage());
    }

    @Test
    void constructor_listNull_thiNemLoiInvalidData() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Polynomial(1, null)
        );
        assertEquals("Invalid Data", ex.getMessage());
    }

    @Test
    void cal_daThucBac0() {
        Polynomial p = new Polynomial(0, List.of(7)); // P(x)=7
        assertEquals(7, p.cal(-100.0));
        assertEquals(7, p.cal(0.0));
        assertEquals(7, p.cal(2.5));
    }

    @Test
    void cal_daThucDonGian_soNguyenX() {
        // P(x) = 1 + 2x + 3x^2
        Polynomial p = new Polynomial(2, List.of(1, 2, 3));
        assertEquals(1, p.cal(0));
        assertEquals(6, p.cal(1));   // 1+2+3=6
        assertEquals(17, p.cal(2));  // 1+4+12=17
        assertEquals(6, p.cal(-1));  // 1-2+3=2 (khoan: 1 + 2(-1) + 3(1) = 2)
    }

    @Test
    void cal_daThucVoiSoAm() {
        // P(x) = -5 + 0*x + 2*x^2
        Polynomial p = new Polynomial(2, List.of(-5, 0, 2));
        assertEquals(-5, p.cal(0));
        assertEquals(-3, p.cal(1));   // -5 + 2
        assertEquals(3, p.cal(2));    // -5 + 8
        assertEquals(3, p.cal(-2));   // -5 + 8
    }

    @Test
    void cal_voiXThuc_kiemTraDungTheoCachEpKieuIntTungHang() {
        // P(x) = 1 + 1*x
        // x = 0.6 => (int)(1*0.6) = 0 => kết quả = 1
        Polynomial p = new Polynomial(1, List.of(1, 1));
        assertEquals(1, p.cal(0.6));

        // x = 1.6 => (int)(1*1.6)=1 => 1+1=2
        assertEquals(2, p.cal(1.6));
    }
}
