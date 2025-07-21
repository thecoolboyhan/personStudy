package com.状态机;

public enum States {
    SI,S1,S2,SF
}

enum Events{
    E1,E2;
}

enum OrderStates{
//    已提交
    SUBMITTED,
//    已支付
    PAID,
//    已完成
    FULFILLED,
//    已取消
    CANCELLED;
}

enum OrderEvents{
//    支付
    PAY,
//    完成
    FULFILL,
//    取消
    CANCEL
}