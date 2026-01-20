import org.HocVien;
import org.QuanLyHocVien;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuanLyHocVienTest {

    @Test
    void locDanhSachNhanHocBong_dungSoLuongVaDungNguoi() {
        QuanLyHocVien ql = new QuanLyHocVien();

        HocVien a = new HocVien("HV01", "A", "HCM", 8, 8, 8);         // đủ
        HocVien b = new HocVien("HV02", "B", "HN", 10, 10, 4);        // rớt do 1 môn <5
        HocVien c = new HocVien("HV03", "C", "DN", 7, 8, 8);          // rớt do TB <8
        HocVien d = new HocVien("HV04", "D", "HP", 9, 8, 9);          // đủ

        ql.themHocVien(a);
        ql.themHocVien(b);
        ql.themHocVien(c);
        ql.themHocVien(d);

        List<HocVien> ds = ql.danhSachNhanHocBong();

        assertEquals(2, ds.size());
        assertEquals("HV01", ds.get(0).getMaSo());
        assertEquals("HV04", ds.get(1).getMaSo());
    }

    @Test
    void themHocVien_null_thiNemLoi() {
        QuanLyHocVien ql = new QuanLyHocVien();
        assertThrows(IllegalArgumentException.class, () -> ql.themHocVien(null));
    }

    @Test
    void danhSachRong_thiKetQuaRong() {
        QuanLyHocVien ql = new QuanLyHocVien();
        assertTrue(ql.danhSachNhanHocBong().isEmpty());
    }
}
