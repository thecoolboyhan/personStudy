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





# Nginx



## 五种IO模型

- 阻塞IO：当没有获取到资源时，会一直等待，直到获取到资源为止。
- 非阻塞IO：任然只有一个线程在运行，但当没有获得到资源时，不会一直等待，继续执行，等到空闲时再回来判断是否获得的到资源。
- 异步IO: 会开辟一个新的线程，新的线程负责等待，而原来的继续执行
- 信号驱动IO：在获取资源时，会给资源发送一个信号值，当获取到资源时，就会收到一个信号值，然后获取。
- 多通道IO：需要获取资源时，会给多个地方同时发送获取的请求，只要有一个地方可以成功获取到资源，就可以继续执行。



## Tengine

> nginx是一个高性能的HTTP和反向代理服务器。其特点是占用内存少，并发能力强。



## nginx和apache的区别

### nginx相对于apache的优点

- 轻量级： 同样起web服务，比apache占用更少的内存和资源
- 抗并发：nginx处理请求为异步非阻塞的，而apache则是阻塞的，在高并发下nginx可以保持低资源，低消耗，高性能。
- 高度模块化的设计，编写模块相对简单。
- 社区活跃，各种高效能模块出品迅速。

### apache相对于nginx的优点：

> rewrite：重写url

- rewrite：比nginx的rewrite强大
- 模块超多，基本想到的都可以找到。
- 少bug



nginx配置简洁，apache复杂

> 最核心的区别在于apache是同步多进程模型，一个连接对应一个进程，nginx是异步的，多个连接（万级别）可以对应一个进程



- nginx性能高的原因

nginx 当请求数超过最大进程数时，它会在一个进程中开辟一个新线程，由于nginx是异步非阻塞的，此非阻塞IO操作是由操作系统来执行的，而apache是阻塞的



- sendfile off/on（异步网络IO）

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624260167667-1624260167658.png)

off时：在网络传输文件中，文件会先被IO操作读到APP，然后APP再通过一次IO操作推给内核去进行网络传输。

on时，会给内核发送一个指令，让内核直接去读文件，然后传输。（异步网络IO）





## 反向代理

> 用户发送请求给代理代理服务器，代理服务在把这些请求分发给后面的服务器

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624416591118-1624416591103.png)

### upsteam

给需要负载的服务器分组

如果只写server就是默认轮巡（一人一下）

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624426250073-1624426250060.png)

### 权重

给可以通过weight属性给负载的服务器设置权重，权重（1-10）高的服务器被负载的几率大。

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624426551982-1624426551978.png)

### session共享

tomcat可以通过Memcache来实现多台服务器共享同一个session

