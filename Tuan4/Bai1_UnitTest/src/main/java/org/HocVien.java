package org;

public class HocVien {
    private final String maSo;
    private final String hoTen;
    private final String queQuan;
    private final double diem1;
    private final double diem2;
    private final double diem3;

    public HocVien(String maSo, String hoTen, String queQuan, double diem1, double diem2, double diem3) {
        if (maSo == null || maSo.isBlank()) throw new IllegalArgumentException("Invalid Data");
        if (hoTen == null || hoTen.isBlank()) throw new IllegalArgumentException("Invalid Data");
        if (queQuan == null) throw new IllegalArgumentException("Invalid Data");
        this.maSo = maSo;
        this.hoTen = hoTen;
        this.queQuan = queQuan;
        this.diem1 = diem1;
        this.diem2 = diem2;
        this.diem3 = diem3;
    }

    public String getMaSo() { return maSo; }
    public String getHoTen() { return hoTen; }
    public String getQueQuan() { return queQuan; }
    public double getDiem1() { return diem1; }
    public double getDiem2() { return diem2; }
    public double getDiem3() { return diem3; }

    public double diemTrungBinh() {
        return (diem1 + diem2 + diem3) / 3.0;
    }

    public boolean duDieuKienHocBong() {
        // TB >= 8.0 và không môn nào dưới 5
        return diemTrungBinh() >= 8.0
                && diem1 >= 5.0
                && diem2 >= 5.0
                && diem3 >= 5.0;
    }
}
