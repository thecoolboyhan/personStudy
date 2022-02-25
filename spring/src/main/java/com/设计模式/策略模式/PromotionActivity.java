package com.设计模式.策略模式;

public class PromotionActivity {
    private CouponStrategy couponStrategy;
    public PromotionActivity(CouponStrategy couponStrategy){
        this.couponStrategy=couponStrategy;
    }

    public void execute(){
        couponStrategy.doPromotion();
    }
}
