package 重学设计模式.Section4工厂模式.good;

import java.util.Map;

public interface ICommodity {
    void sendCommodity(String uId, String commodityId, String bizId, Map<String, String> extMap) throws Exception;
}
