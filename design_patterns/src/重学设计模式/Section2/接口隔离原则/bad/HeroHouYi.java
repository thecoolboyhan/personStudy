package 重学设计模式.Section2.接口隔离原则.bad;

public class HeroHouYi implements ISkill{

    @Override
    public void doArchery() {
        System.out.println("11");
    }

    @Override
    public void doInvisible() {
        System.out.println("11");
    }

    @Override
    public void doSilent() {
        System.out.println("11");
    }

    @Override
    public void doVertigo() {

    }
}
