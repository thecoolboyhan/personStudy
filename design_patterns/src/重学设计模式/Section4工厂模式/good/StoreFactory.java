package 重学设计模式.Section4工厂模式.good;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//商品工厂，得到不同类型的服务
public class StoreFactory {

    public ICommodity getCommodityService(Integer commodityType) {
        if (null == commodityType) return null;
        if (1 == commodityType) return new CouponCommodityService();
        if (2 == commodityType) return new GoodsCommodityService();
        if (3 == commodityType) return new CardCommodityService();
        throw new RuntimeException("不存在的商品服务类型");
    }

    public static void main(String[] args) {
        List<Integer>[] ll=new List[2];
        Arrays.setAll(ll,i->new ArrayList<Integer>());
    }
}
