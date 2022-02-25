package com.设计模式.策略模式;

import java.util.HashMap;
import java.util.Map;

public class PromotionStrategyFactory {
    private static Map<String ,CouponStrategy> MAP =new HashMap<>();
    static {
        MAP.put(PromotionKey.CASH,new CashbackStrategy());
        MAP.put(PromotionKey.CUN,new CunponStrategy());
    }

    private static final CouponStrategy NO_PROMOTION=new EmptyStrategy();
    private PromotionStrategyFactory(){}
    public static CouponStrategy getCouponStrategy(String promotionKey){
        CouponStrategy couponStrategy = MAP.get(promotionKey);
        return couponStrategy==null? NO_PROMOTION:couponStrategy;
    }
    private interface PromotionKey{
        String CUN="CUN";
        String CASH="CASH";
    }
}
