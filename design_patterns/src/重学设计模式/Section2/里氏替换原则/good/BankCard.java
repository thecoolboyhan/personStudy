package 重学设计模式.Section2.里氏替换原则.good;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// 抽象银行卡类
public abstract class BankCard {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String cardNo;
    private String cardDate;

    public BankCard(String cardNo, String cardDate) {
        this.cardNo = cardNo;
        this.cardDate = cardDate;
    }
    abstract boolean rule(BigDecimal amount);

    public String positive(String orderId, BigDecimal amount) {
//        存款成功
        logger.info(STR."卡号\{cardNo}存款成功，单号：\{orderId}，金额：\{amount}");
        return "0000";
    }

    public String negative(String orderId, BigDecimal amount) {
        logger.info(STR."卡号：\{cardNo}出款成功，单号：\{orderId}，金额：\{amount}");
        return "0000";
    }
    //    交易流水查询
    public List<String> tradeFlow(){
        logger.info("交易流水查询成功");
        List<String> tradeList = new ArrayList<>();
        tradeList.add("100001,100.00");
        tradeList.add("100001,80.00");
        tradeList.add("100001,76.50");
        tradeList.add("100001,126.00");
        return tradeList;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardDate() {
        return cardDate;
    }
}
