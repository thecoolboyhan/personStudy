> 所有交给spring管理的对象默认都是单例的，new对象是一个非常消耗性能的操作。

在这些spring管理的类中，最好不要有状态属性，如果有的话一定要非常小心的使用，由于他们都是单例的，所以线程不安全，



# 动态代理

- spring 的动态代理是由ASM来实现的

## JDK动态代理

> jdk动态代理使用的是java的反射机制来实现的，jdk的动态代理需要被代理的类实现一个接口，然后继承接口。

``` java
public static void main(String[] args) {

        final GirlDJ girlDJ = new GirlDJ();
        /**
         * InvocationHandler:拦截结果对于拦截的结果进行处理
         */
        ProGirlDJ proGirlDJ  = (ProGirlDJ) Proxy.newProxyInstance(GirlDJ.class.getClassLoader(), GirlDJ.class.getInterfaces(), new InvocationHandler() {
            /**
             *
             * @param proxy :代理对象
             * @param method: 执行的方法
             * @param args：参数
             * @return
             * @throws Throwable
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                if (method.getName().endsWith("bath")) {
                    System.out.println("偷看前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("偷看后");
                    return invoke;
                } else {
                    System.out.println("饭前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("饭后");
                    return invoke;
                }
            }
        });

        proGirlDJ.bath();
        proGirlDJ.eat();
    }
```



## cglib动态代理

> cglib动态代理是继承需要代理的类，通过增强器来实现动态代理，增强器会继承代理类，cglib的好处是要代理的类不用实现接口。



``` java
public class CGLibFactory implements MethodInterceptor {

    private Object object;

    public CGLibFactory() {
    }

    public CGLibFactory(Object object) {
        super();
        this.object = object;
    }

    public Object createProxy(){
        //增强器
        Enhancer enhancer = new Enhancer();
        //这是增强器的父类
        enhancer.setSuperclass(object.getClass());
        //回调
        enhancer.setCallback(this);
        return enhancer.create();
    }


    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前");
        method.invoke(object,objects);
        System.out.println("后");
        return null;
    }
}

```



# ASM



