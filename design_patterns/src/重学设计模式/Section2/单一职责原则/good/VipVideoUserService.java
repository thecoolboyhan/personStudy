package 重学设计模式.Section2.单一职责原则.good;

public class VipVideoUserService implements IVideoUserService{
    @Override
    public void definition() {
        System.out.println("1080P");
    }

    @Override
    public void advertisement() {
        System.out.println("VIP没有广告");
    }
}
