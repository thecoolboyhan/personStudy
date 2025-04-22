package 重学设计模式.Section2.接口隔离原则.bad;

public class HeroLianPo implements ISkill{
    @Override
    public void doArchery() {

    }

    @Override
    public void doInvisible() {
        System.out.println("22");
    }

    @Override
    public void doSilent() {
        System.out.println("23");
    }

    @Override
    public void doVertigo() {
        System.out.println("24");
    }
}
