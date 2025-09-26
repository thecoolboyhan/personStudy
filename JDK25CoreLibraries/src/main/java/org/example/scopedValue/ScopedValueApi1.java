package org.example.scopedValue;

import static java.lang.ScopedValue.where;

public class ScopedValueApi1 {

    private static final ScopedValue<String> X=ScopedValue.newInstance();
    void foo(){
//       false：检测是否存在值
        System.out.println(X.isBound());
        where(X,"Hello").run(()->bar());
//        没有值会直接报错
//        System.out.println(X.get());

    }
    void bar(){
//       打印父线程传递的值
//        Hello接收到父线程传递的hello
        System.out.println(X.get());
        where(X,"goodbye").run(()->baz());
//        Hello:把goodbye传递给了baz，但自己不受到影响
        System.out.println(X.get());
    }
    void baz(){
        System.out.println(X.get());
    }
    public static void main(String[] args) {
        ScopedValueApi1 scopedValueApi1 = new ScopedValueApi1();
        scopedValueApi1.foo();
    }
}
