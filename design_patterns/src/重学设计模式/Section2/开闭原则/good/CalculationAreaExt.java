package 重学设计模式.Section2.开闭原则.good;

import 重学设计模式.Section2.开闭原则.bad.CalculationArea;

public class CalculationAreaExt extends CalculationArea {

//    在子类中做出修改，需要用到修改的后的方法调用子类，不需要的部分保持不变。
    private final  static double PI=3.141592653D;

    @Override
    public double circular(double r) {
        return PI*r*r;
    }
}
