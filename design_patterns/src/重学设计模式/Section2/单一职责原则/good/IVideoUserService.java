package 重学设计模式.Section2.单一职责原则.good;

public interface IVideoUserService {
//    清晰度
    void definition();
//    是否有广告
    void advertisement();


    public static void main(String[] args) {
//        扩展只需要新建实现类，互不相关
        IVideoUserService guest = new GuestVideoUserService();
        guest.definition();
        guest.advertisement();

        IVideoUserService ordinary = new OrdinaryVideoUserService();
        ordinary.definition();
        ordinary.advertisement();

        IVideoUserService vip=new VipVideoUserService();
        vip.definition();
        vip.advertisement();
    }
}
