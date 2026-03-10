package service;

public class XepLoaiService {

    public static String xepLoai(double diemTB, boolean coThiLai) {

        if (diemTB < 0 || diemTB > 10) { // C1
            return "Diem khong hop le";
        }

        if (diemTB >= 8.5) { // C2
            return "Gioi";
        }

        else if (diemTB >= 7.0) { // C3
            return "Kha";
        }

        else if (diemTB >= 5.5) { // C4
            return "Trung Binh";
        }

        else {

            if (coThiLai) { // C5
                return "Thi lai";
            }

            return "Yeu - Hoc lai";
        }
    }
}