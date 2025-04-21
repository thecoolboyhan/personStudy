package 重学设计模式.Section2.里氏替换原则.bad;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class CreditCard extends CashCard{

//存在的问题：子类信用卡破坏了父类原有的提现功能，如果父类卡使用子类来提现将无法实现。不符合里氏替换原则
    private final Logger logger=Logger.getLogger(this.getClass().getName());

//    提现
    @Override
    public String withdrawal(String orderId, BigDecimal amount) {
//        校验
        if(amount.compareTo(new BigDecimal(1000))>=0){
            logger.info(STR."提现限额1000，单号\{orderId}金额：\{amount}");
            return "0001";
        }
        logger.info(STR."贷款成功，单号：\{orderId}金额：\{amount}");
        return "0000";
    }

    @Override
    public String recharge(String orderId, BigDecimal amount) {
//        模拟还款
        logger.info(STR."还款成功，单号:\{orderId}金额：\{amount}");
        return "0000";
    }
}
