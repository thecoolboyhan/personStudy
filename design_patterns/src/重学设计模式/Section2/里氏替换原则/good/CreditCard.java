package 重学设计模式.Section2.里氏替换原则.good;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class CreditCard extends CashCard{
    private final Logger logger=Logger.getLogger(getClass().getName());
    public CreditCard(String cardNo, String cardDate) {
        super(cardNo, cardDate);
    }

    boolean rule2(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(1000))<=0;
    }

//    提现
    public String loan(String orderId,BigDecimal amount) {
        boolean rule=rule2(amount);
        if(!rule){
            logger.info(STR."生成贷款单失败,金额超限：\{orderId}，金额：\{amount}");
            return "0001";
        }
        logger.info(STR."贷款成功，单号：\{orderId}，金额：\{amount}");
        return super.negative(orderId,amount);
    }

}
