package com.设计模式.策略模式;

public class CunponStrategy implements CouponStrategy {
    @Override
    public void doPromotion() {
        System.out.println("优惠卷");
    }
}
