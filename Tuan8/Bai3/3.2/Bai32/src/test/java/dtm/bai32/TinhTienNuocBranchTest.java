package dtm.bai32;

import dtm.bai32.service.TinhTienNuocService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TinhTienNuocBranchTest {

    @Test
    public void testInvalidVolume() {

        double result = TinhTienNuocService.tinhTienNuoc(0, "dan_cu");

        Assert.assertEquals(result, 0);
    }


    @Test
    public void testHoNgheo() {

        double result = TinhTienNuocService.tinhTienNuoc(5, "ho_ngheo");

        Assert.assertEquals(result, 25000);
    }


    @Test
    public void testDanCuLevel1() {

        double result = TinhTienNuocService.tinhTienNuoc(8, "dan_cu");

        Assert.assertEquals(result, 60000);
    }


    @Test
    public void testDanCuLevel2() {

        double result = TinhTienNuocService.tinhTienNuoc(15, "dan_cu");

        Assert.assertEquals(result, 148500);
    }


    @Test
    public void testKinhDoanh() {

        double result = TinhTienNuocService.tinhTienNuoc(25, "kinh_doanh");

        Assert.assertEquals(result, 550000);
    }
}