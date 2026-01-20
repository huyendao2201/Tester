import org.Diem;
import org.HinhChuNhat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HinhChuNhatTest {

    private static final double EPS = 1e-9;

    @Test
    void dienTich_hinhBinhThuong() {
        // trên-trái (1, 5), dưới-phải (6, 2) => width=5, height=3 => area=15
        HinhChuNhat r = new HinhChuNhat(new Diem(1, 5), new Diem(6, 2));
        assertEquals(15.0, r.dienTich(), EPS);
    }

    @Test
    void dienTich_hinhCoDienTich0() {
        // chiều rộng = 0
        HinhChuNhat r1 = new HinhChuNhat(new Diem(2, 5), new Diem(2, 1));
        assertEquals(0.0, r1.dienTich(), EPS);

        // chiều cao = 0
        HinhChuNhat r2 = new HinhChuNhat(new Diem(2, 3), new Diem(7, 3));
        assertEquals(0.0, r2.dienTich(), EPS);
    }

    @Test
    void constructor_duLieuSai_thiNemLoi() {
        // trên-trái x > dưới-phải x
        assertThrows(IllegalArgumentException.class,
                () -> new HinhChuNhat(new Diem(5, 5), new Diem(2, 2)));

        // trên-trái y < dưới-phải y
        assertThrows(IllegalArgumentException.class,
                () -> new HinhChuNhat(new Diem(1, 1), new Diem(3, 4)));

        // null
        assertThrows(IllegalArgumentException.class,
                () -> new HinhChuNhat(null, new Diem(1, 1)));
    }

    @Test
    void giaoNhau_giaoNhauMotPhan() {
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(2, 3), new Diem(6, -1));
        assertTrue(a.giaoNhau(b));
        assertTrue(b.giaoNhau(a));
    }

    @Test
    void giaoNhau_motCaiNamTrongCaiKia() {
        HinhChuNhat big = new HinhChuNhat(new Diem(0, 10), new Diem(10, 0));
        HinhChuNhat small = new HinhChuNhat(new Diem(2, 8), new Diem(4, 6));
        assertTrue(big.giaoNhau(small));
        assertTrue(small.giaoNhau(big));
    }

    @Test
    void giaoNhau_khongGiaoNhau_viNamRoiNhau() {
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(5, 4), new Diem(8, 0));
        assertFalse(a.giaoNhau(b));
        assertFalse(b.giaoNhau(a));
    }

    @Test
    void giaoNhau_chamCanh_khongTinhGiaoNhau_theoCodeHienTai() {
        // a: [0..4]x[0..4], b: chạm cạnh phải của a tại x=4
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(4, 3), new Diem(7, 1));
        assertFalse(a.giaoNhau(b));
    }

    @Test
    void giaoNhau_chamGoc_khongTinhGiaoNhau_theoCodeHienTai() {
        // chạm đúng 1 điểm (4,0)
        HinhChuNhat a = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
        HinhChuNhat b = new HinhChuNhat(new Diem(4, 0), new Diem(6, -2));
        assertFalse(a.giaoNhau(b));
    }
}
