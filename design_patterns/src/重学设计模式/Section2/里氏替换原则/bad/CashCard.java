package 重学设计模式.Section2.里氏替换原则.bad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//储蓄卡
public class CashCard {
    private Logger log = Logger.getLogger(this.getClass().getName());

//    提现
    public String withdrawal(String orderId, BigDecimal amount) {
        log.info(STR."提现成功，单号：\{orderId}金额：\{amount}");
        return "0000";
    }

//    储蓄
    public String recharge(String orderId, BigDecimal amount) {
        log.info(STR."储蓄成功，单号：\{orderId}金额：\{amount}");
        return "0000";
    }

//    交易流水查询
    public List<String> tradeFlow(){
        log.info("交易流水查询成功");
        List<String> tradeList = new ArrayList<>();
        tradeList.add("100001,100.00");
        tradeList.add("100001,80.00");
        tradeList.add("100001,76.50");
        tradeList.add("100001,126.00");
        return tradeList;
    }
}
