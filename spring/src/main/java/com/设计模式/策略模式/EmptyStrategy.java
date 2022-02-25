package com.设计模式.策略模式;

public class EmptyStrategy implements CouponStrategy{
    @Override
    public void doPromotion() {
        System.out.println("无优惠");
    }

    public static void main(String[] args) {
        String promotionKey="CUN";
        PromotionActivity promotionActivity = new PromotionActivity(PromotionStrategyFactory.getCouponStrategy(promotionKey));
        promotionActivity.execute();
    }
}
