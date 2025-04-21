package 重学设计模式.Section2.开闭原则.bad;

import 重学设计模式.Section2.开闭原则.ICalculationArea;

public class CalculationArea implements ICalculationArea {

//    如果想要修订PI的值，不应该直接在这里做出修改

    private final static double PI=3.14D;
    @Override
    public double rectangle(double x, double y) {
        return x*y;
    }

    @Override
    public double triangle(double x, double y, double z) {
        double p=(x+y+z)/2;
        return Math.sqrt(p*(p-x)*(p-y)*(p-z));
    }

    @Override
    public double circular(double r) {
        return PI*r*r;
    }
}
