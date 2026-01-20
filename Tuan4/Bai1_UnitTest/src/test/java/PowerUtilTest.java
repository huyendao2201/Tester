import org.PowerUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUtilTest {

    private static final double EPS = 1e-9;

    @Test
    void nBang0_traVe1() {
        assertEquals(1.0, PowerUtil.power(2.5, 0), EPS);
        assertEquals(1.0, PowerUtil.power(-3.0, 0), EPS);
        assertEquals(1.0, PowerUtil.power(0.0, 0), EPS);
    }

    @Test
    void nDuong_tinhDungVoiSoNguyenNho() {
        assertEquals(8.0, PowerUtil.power(2.0, 3), EPS);
        assertEquals(1.0, PowerUtil.power(1.0, 7), EPS);
        assertEquals(-8.0, PowerUtil.power(-2.0, 3), EPS);
        assertEquals(16.0, PowerUtil.power(-2.0, 4), EPS);
    }

    @Test
    void nAm_tinhDungVoiSoNguyenNho() {
        assertEquals(0.5, PowerUtil.power(2.0, -1), EPS);
        assertEquals(0.125, PowerUtil.power(2.0, -3), EPS);
        assertEquals(-0.125, PowerUtil.power(-2.0, -3), EPS);
        assertEquals(1.0 / 16.0, PowerUtil.power(2.0, -4), EPS);
    }

    @Test
    void soSanhVoiMathPow_voiNhieuGiaTri() {
        double[] xs = { -3.0, -1.5, -1.0, -0.5, 0.5, 1.2, 2.0, 3.5 };
        int[] ns = { -5, -3, -1, 0, 1, 2, 4, 6 };

        for (double x : xs) {
            for (int n : ns) {
                // tránh trường hợp chia cho 0 khi n < 0
                if (x == 0.0 && n < 0) continue;

                double expected = Math.pow(x, n);
                double actual = PowerUtil.power(x, n);
                assertEquals(expected, actual, 1e-7, "x=" + x + ", n=" + n);
            }
        }
    }

    @Test
    void tinhChat_nghichDao_khiXKhac0() {
        double x = 3.0;
        int n = 7;

        double a = PowerUtil.power(x, n);
        double b = PowerUtil.power(x, -n);

        assertEquals(1.0, a * b, 1e-6);
    }

    @Test
    void xBang0_nDuong_traVe0() {
        assertEquals(0.0, PowerUtil.power(0.0, 1), EPS);
        assertEquals(0.0, PowerUtil.power(0.0, 5), EPS);
    }

    @Test
    void xBang0_nAm_ketQuaLaInfinityTheoIEEE754() {
        assertTrue(Double.isInfinite(PowerUtil.power(0.0, -1)));
        assertTrue(Double.isInfinite(PowerUtil.power(0.0, -3)));
    }
}
