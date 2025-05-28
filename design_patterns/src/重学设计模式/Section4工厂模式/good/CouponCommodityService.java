package 重学设计模式.Section4工厂模式.good;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
//优惠券
public class CouponCommodityService implements ICommodity {

    private Logger logger = LoggerFactory.getLogger(CouponCommodityService.class);

//    private CouponService couponService = new CouponService();

    public void sendCommodity(String uId, String commodityId, String bizId, Map<String, String> extMap) throws Exception {
        /**
        CouponResult couponResult = couponService.sendCoupon(uId, commodityId, bizId);
        logger.info("请求参数[优惠券] => uId：{} commodityId：{} bizId：{} extMap：{}", uId, commodityId, bizId, JSON.toJSON(extMap));
        logger.info("测试结果[优惠券]：{}", JSON.toJSON(couponResult));
        if (!"0000".equals(couponResult.getCode())) throw new RuntimeException(couponResult.getInfo());
         **/
    }

}