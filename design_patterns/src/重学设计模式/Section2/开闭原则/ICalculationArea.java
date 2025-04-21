package 重学设计模式.Section2.开闭原则;

public interface ICalculationArea {

//    长方形
    double rectangle(double x,double y);

//    三角形
    double triangle(double x,double y, double z);

//    圆
    double circular(double r);
}
