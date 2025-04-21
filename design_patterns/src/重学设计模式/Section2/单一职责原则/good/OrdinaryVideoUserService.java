package 重学设计模式.Section2.单一职责原则.good;

public class OrdinaryVideoUserService implements IVideoUserService{
    @Override
    public void definition() {
        System.out.println("720P");
    }

    @Override
    public void advertisement() {
        System.out.println("普通用户、有广告");
    }
}
