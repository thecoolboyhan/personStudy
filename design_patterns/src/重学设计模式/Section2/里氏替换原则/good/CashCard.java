package 重学设计模式.Section2.里氏替换原则.good;

import java.math.BigDecimal;

public class CashCard extends  BankCard{

    public CashCard(String cardNo, String cardDate) {
        super(cardNo, cardDate);
    }

    @Override
    boolean rule(BigDecimal amount) {
        return true;
    }

//    风控，储蓄卡默认通过
    public boolean checkRisk(String cardNo,String orderId,BigDecimal amount) {
        return true;
    }
}
