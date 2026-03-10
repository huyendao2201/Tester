package dtm.bai3;

import org.testng.Assert;
import org.testng.annotations.Test;
import service.XepLoaiService;

public class XepLoaiBranchCoverageTest {

    // BC1
    @Test
    public void testInvalidScore() {

        String result = XepLoaiService.xepLoai(-1, false);

        Assert.assertEquals(result, "Diem khong hop le");
    }


    // BC2
    @Test
    public void testGioi() {

        String result = XepLoaiService.xepLoai(9, false);

        Assert.assertEquals(result, "Gioi");
    }


    // BC3
    @Test
    public void testKha() {

        String result = XepLoaiService.xepLoai(7.5, false);

        Assert.assertEquals(result, "Kha");
    }


    // BC4
    @Test
    public void testTrungBinh() {

        String result = XepLoaiService.xepLoai(6, false);

        Assert.assertEquals(result, "Trung Binh");
    }


    // BC5
    @Test
    public void testThiLai() {

        String result = XepLoaiService.xepLoai(4, true);

        Assert.assertEquals(result, "Thi lai");
    }


    // BC6
    @Test
    public void testHocLai() {

        String result = XepLoaiService.xepLoai(4, false);

        Assert.assertEquals(result, "Yeu - Hoc lai");
    }

}