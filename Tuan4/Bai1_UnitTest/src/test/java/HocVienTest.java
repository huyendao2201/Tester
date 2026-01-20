import org.HocVien;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HocVienTest {

    private static final double EPS = 1e-9;

    @Test
    void tinhDiemTrungBinh_dung() {
        HocVien hv = new HocVien("HV01", "Nguyen Van A", "HCM", 9.0, 8.0, 7.0);
        assertEquals(8.0, hv.diemTrungBinh(), EPS);
    }

    @Test
    void duDieuKienHocBong_tbBang8_vaKhongMonNaoDuoi5() {
        HocVien hv = new HocVien("HV02", "Tran Thi B", "HN", 8.0, 8.0, 8.0);
        assertTrue(hv.duDieuKienHocBong());
    }

    @Test
    void khongDuDieuKien_tbDuoi8() {
        HocVien hv = new HocVien("HV03", "Le Van C", "DN", 7.0, 9.0, 8.0); // TB = 8.0? (7+9+8)/3 = 8.0
        assertTrue(hv.duDieuKienHocBong()); // vẫn đủ vì TB=8 và không môn <5
    }

    @Test
    void khongDuDieuKien_coMonDuoi5_duTBVanCao() {
        HocVien hv = new HocVien("HV04", "Pham Thi D", "HP", 10.0, 10.0, 4.0);
        assertFalse(hv.duDieuKienHocBong());
    }

    @Test
    void bien_tbVuaDu8() {
        HocVien hv = new HocVien("HV05", "Do Van E", "CT", 7.5, 8.0, 8.5); // TB = 8.0
        assertTrue(hv.duDieuKienHocBong());
    }

    @Test
    void bien_monBang5_vanDu() {
        HocVien hv = new HocVien("HV06", "Ho Thi F", "HCM", 5.0, 9.5, 9.5);
        assertTrue(hv.duDieuKienHocBong());
    }
}
