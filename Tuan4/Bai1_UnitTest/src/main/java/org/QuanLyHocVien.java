package org;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuanLyHocVien {
    private final List<HocVien> ds = new ArrayList<>();

    public void themHocVien(HocVien hv) {
        if (hv == null) throw new IllegalArgumentException("Invalid Data");
        ds.add(hv);
    }

    public List<HocVien> getDanhSach() {
        return Collections.unmodifiableList(ds);
    }

    public List<HocVien> danhSachNhanHocBong() {
        List<HocVien> result = new ArrayList<>();
        for (HocVien hv : ds) {
            if (hv.duDieuKienHocBong()) {
                result.add(hv);
            }
        }
        return result;
    }
}
