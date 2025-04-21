package 重学设计模式.Section2.单一职责原则.good;

public class GuestVideoUserService implements IVideoUserService{
    @Override
    public void definition() {
        System.out.println("480P");
    }

    @Override
    public void advertisement() {
        System.out.println("访客有广告");
    }
}
